//
//  FirmaConCIE.cpp
//  cie-pkcs11
//
//  Created by Pierluigi De Gregorio on 18/02/21.
//  Copyright © 2021 IPZS. All rights reserved.
//

#include "FirmaConCIE.h"
#include "IAS.h"
#include "../PKCS11/wintypes.h"
#include "../PKCS11/PKCS11Functions.h"
#include "../PKCS11/Slot.h"
#include "../Util/ModuleInfo.h"
#include "../PCSC/PCSC.h"
#include "../Crypto/ASNParser.h"
#include "../Sign/CIESign.h"
#include "AbilitaCIE.h"


#define CARD_PAN_MISMATCH            (int)(0x000000F1)

extern "C" {
    CK_RV CK_ENTRY firmaConCIE(const char* inFilePath, const char* type, const char* pin, const char* pan, int page, float x, float y, float w, float h, const char* imagePathFile, const char* outFilePath, PROGRESS_CALLBACK progressCallBack, SIGN_COMPLETED_CALLBACK completedCallBack);
}


CK_RV CK_ENTRY firmaConCIE(const char* inFilePath, const char* type, const char* pin, const char* pan, int page, float x, float y, float w, float h, const char* imagePathFile, const char* outFilePath, PROGRESS_CALLBACK progressCallBack, SIGN_COMPLETED_CALLBACK completedCallBack)
{

    printf("page: %d, x: %f, y: %f, w: %f, h: %f", page, x, y, w, h);

    char* readers = NULL;
    char* ATR = NULL;
    try
    {
        std::map<uint8_t, ByteDynArray> hashSet;

        DWORD len = 0;
        ByteDynArray CertCIE;
        ByteDynArray SOD;

        SCARDCONTEXT hSC;

        long nRet = SCardEstablishContext(SCARD_SCOPE_USER, nullptr, nullptr, &hSC);
        if (nRet != SCARD_S_SUCCESS)
            return CKR_DEVICE_ERROR;

        OutputDebugString("Establish Context ok\n");

        if (SCardListReaders(hSC, nullptr, NULL, &len) != SCARD_S_SUCCESS) {
            OutputDebugString("List readers ko\n");
            return CKR_TOKEN_NOT_PRESENT;
        }

        if (len == 1)
            return CKR_TOKEN_NOT_PRESENT;

        readers = (char*)malloc(len);

        if (SCardListReaders(hSC, nullptr, (char*)readers, &len) != SCARD_S_SUCCESS) {
            free(readers);
            return CKR_TOKEN_NOT_PRESENT;
        }

        char *curreader = readers;
        bool foundCIE = false;
        for (; curreader[0] != 0; curreader += strnlen(curreader, len) + 1)
        {
            safeConnection conn(hSC, curreader, SCARD_SHARE_SHARED);
            if (!conn.hCard)
                continue;

            LONG res = 0;

			DWORD atrLen = 40;
			res = SCardGetAttrib(conn.hCard, SCARD_ATTR_ATR_STRING, (uint8_t*)ATR, &atrLen);
			if (res != SCARD_S_SUCCESS) {
				free(readers);
				OutputDebugString("GetAttrib ko 1, %d\n", res);
				return CKR_DEVICE_ERROR;
			}


			ATR = (char*)malloc(atrLen);

			if (SCardGetAttrib(conn.hCard, SCARD_ATTR_ATR_STRING, (uint8_t*)ATR, &atrLen) != SCARD_S_SUCCESS) {
				free(readers);
				free(ATR);
				return CKR_DEVICE_ERROR;
			}

            ByteArray atrBa((BYTE*)ATR, atrLen);

            progressCallBack(20, "");

            IAS* ias = new IAS((CToken::TokenTransmitCallback)TokenTransmitCallback, atrBa);
            ias->SetCardContext(&conn);

            foundCIE = false;
            ias->token.Reset();
            ias->SelectAID_IAS();
            ias->ReadPAN();

            foundCIE = true;
            ByteDynArray IntAuth;
            ias->SelectAID_CIE();
            ias->ReadDappPubKey(IntAuth);
            ias->SelectAID_CIE();
            ias->InitEncKey();

            ByteDynArray IdServizi;
            ias->ReadIdServizi(IdServizi);
            ByteArray baPan = ByteArray((uint8_t*)pan, strlen(pan));

            if (memcmp(baPan.data(), IdServizi.data(), IdServizi.size()) != 0)
            {
                return CARD_PAN_MISMATCH;
            }

            ByteDynArray FullPIN;
            ByteArray LastPIN = ByteArray((uint8_t*)pin, strlen(pin));
            ias->GetFirstPIN(FullPIN);
            FullPIN.append(LastPIN);
            ias->token.Reset();

            progressCallBack(40, "");

            char fullPinCStr[9];
            memcpy(fullPinCStr, FullPIN.data(), 8);
            fullPinCStr[8] = 0;

            CIESign* cieSign = new CIESign(ias);

            uint16_t ret = cieSign->sign(inFilePath, type, fullPinCStr, page, x, y, w, h, imagePathFile, outFilePath);
            if((ret & (0x63C0)) == 0x63C0)
            {
                return CKR_PIN_INCORRECT;
            }else if (ret == 0x6983)
            {
                return CKR_PIN_LOCKED;
            }


            progressCallBack(100, "");

            OutputDebugString("CieSign ret: %d", ret);

            free(ias);
            free(cieSign);

            completedCallBack(ret);
        }

        if (!foundCIE) {
            free(ATR);
            free(readers);
            return CKR_TOKEN_NOT_RECOGNIZED;

        }
    }
    catch (std::exception &ex) {
        OutputDebugString(ex.what());
        if (ATR)
            free(ATR);
        OutputDebugString("Eccezione: %s", ex.what());
        if (readers)
            free(readers);

        OutputDebugString("General error\n");
        return CKR_GENERAL_ERROR;
    }

    if (ATR)
        free(ATR);

    free(readers);
    return SCARD_S_SUCCESS;
}
