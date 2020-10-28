//
//  AbilitaCIE.cpp
//  cie-pkcs11
//
//  Created by ugo chirico on 06/10/18. http://www.ugochirico.com
//  Copyright © 2018 IPZS. All rights reserved.
//
#include <string.h>
#include "IAS.h"
#include "../PKCS11/wintypes.h"
#include "../PKCS11/PKCS11Functions.h"
#include "../PKCS11/Slot.h"
#include "../Util/ModuleInfo.h"
#include "../Crypto/sha256.h"
#include "../Crypto/sha512.h"
#include <functional>
#include "../Crypto/ASNParser.h"
#include "../PCSC/PCSC.h"
#include <string>
#include "AbilitaCIE.h"
#include <string>
#include "../Cryptopp/misc.h"

#include "../Crypto/ASNParser.h"
#include <stdio.h>
#include "../Crypto/AES.h"
#include "../Cryptopp/cryptlib.h"
#include "../Cryptopp/asn.h"
#include "../Util/CryptoppUtils.h"
#include "../Crypto/CryptoUtil.h"

#include <unistd.h>
#include <sys/socket.h>    //socket
#include <arpa/inet.h>    //inet_addr
#include <sys/types.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

#define ROLE_USER 				1
#define ROLE_ADMIN 				2
#define CARD_ALREADY_ENABLED	0x000000F0;

OID OID_SURNAME = ((OID(2) += 5) += 4) += 4;

OID OID_GIVENNAME = ((OID(2) += 5) += 4) += 42;

extern CModuleInfo moduleInfo;

void GetCertInfo(CryptoPP::BufferedTransformation & certin,
                 std::string & serial,
                 CryptoPP::BufferedTransformation & issuer,
                 CryptoPP::BufferedTransformation & subject,
                 std::string & notBefore,
                 std::string & notAfter,
                 CryptoPP::Integer& mod,
                 CryptoPP::Integer& pubExp);

std::vector<word32> fromObjectIdentifier(std::string sObjId);

int TokenTransmitCallback(safeConnection *data, uint8_t *apdu, DWORD apduSize, uint8_t *resp, DWORD *respSize);

DWORD CardAuthenticateEx(IAS*       ias,
                        DWORD       PinId,
                        DWORD       dwFlags,
                        BYTE*       pbPinData,
                        DWORD       cbPinData,
                        BYTE*       *ppbSessionPin,
                        DWORD*      pcbSessionPin,
						PROGRESS_CALLBACK progressCallBack,
                        int*        pcAttemptsRemaining);

extern "C" {
    CK_RV CK_ENTRY AbilitaCIE(const char*  szPAN, const char*  szPIN, int* attempts, PROGRESS_CALLBACK progressCallBack, COMPLETED_CALLBACK completedCallBack);
    CK_RV CK_ENTRY VerificaCIEAbilitata(const char*  szPAN);
    CK_RV CK_ENTRY DisabilitaCIE(const char*  szPAN);
}

CK_RV CK_ENTRY VerificaCIEAbilitata(const char*  szPAN)
{
            
	if(IAS::IsEnrolled(szPAN))
		return 1;
	else
		return 0;
    
}


CK_RV CK_ENTRY DisabilitaCIE(const char*  szPAN)
{
	if(IAS::IsEnrolled(szPAN))
	{
		IAS::Unenroll(szPAN);
		return CKR_OK;
	}

	return CKR_FUNCTION_FAILED;
}

CK_RV CK_ENTRY AbilitaCIE(const char*  szPAN, const char*  szPIN, int* attempts, PROGRESS_CALLBACK progressCallBack, COMPLETED_CALLBACK completedCallBack)
{
    char* readers = NULL;
    char* ATR = NULL;

    // verifica bontà PIN
    if(szPIN == NULL || strnlen(szPIN, 9) != 8)
    {
    	return CKR_PIN_LEN_RANGE;
    }

	size_t i = 0;
	while (i < 8 && (szPIN[i] >= '0' && szPIN[i] <= '9'))
		i++;

	if (i != 8)
		return CKR_PIN_INVALID;

	try
    {
		std::map<uint8_t, ByteDynArray> hashSet;
		
		DWORD len = 0;
		ByteDynArray CertCIE;
		ByteDynArray SOD;
		ByteDynArray IdServizi;
		
		SCARDCONTEXT hSC;

        progressCallBack(1, "Connessione alla CIE");
        
		long nRet = SCardEstablishContext(SCARD_SCOPE_USER, nullptr, nullptr, &hSC);
        if(nRet != SCARD_S_SUCCESS)
            return CKR_DEVICE_ERROR;
        
        if (SCardListReaders(hSC, nullptr, NULL, &len) != SCARD_S_SUCCESS) {
            return CKR_TOKEN_NOT_PRESENT;
        }
        
        readers = (char*)malloc(len);
        
        if (SCardListReaders(hSC, nullptr, (char*)readers, &len) != SCARD_S_SUCCESS) {
            free(readers);
            return CKR_TOKEN_NOT_PRESENT;
        }

        progressCallBack(5, "CIE Connessa");
        
		char *curreader = readers;
		bool foundCIE = false;
		for (; curreader[0] != 0; curreader += strnlen(curreader, len) + 1)
        {
            safeConnection conn(hSC, curreader, SCARD_SHARE_SHARED);
            if (!conn.hCard)
                continue;

            DWORD atrLen = 40;
            if(SCardGetAttrib(conn.hCard, SCARD_ATTR_ATR_STRING, (uint8_t*)ATR, &atrLen) != SCARD_S_SUCCESS) {
                free(readers);
                return CKR_DEVICE_ERROR;
            }
            
            ATR = (char*)malloc(atrLen);
            
            if(SCardGetAttrib(conn.hCard, SCARD_ATTR_ATR_STRING, (uint8_t*)ATR, &atrLen) != SCARD_S_SUCCESS) {
                free(readers);
                free(ATR);
                return CKR_DEVICE_ERROR;
            }
            
            ByteArray atrBa((BYTE*)ATR, atrLen);
            

            progressCallBack(10, "Verifica carta esistente");

            IAS ias((CToken::TokenTransmitCallback)TokenTransmitCallback, atrBa);
            ias.SetCardContext(&conn);
            
            foundCIE = false;
            
            ias.token.Reset();
            ias.SelectAID_IAS();
            ias.ReadPAN();
        
            
            ByteDynArray IntAuth;
            ias.SelectAID_CIE();
            ias.ReadDappPubKey(IntAuth);
            //ias.SelectAID_CIE();
            ias.InitEncKey();
            
            ByteDynArray IdServizi;
            ias.ReadIdServizi(IdServizi);

            if (ias.IsEnrolled())
            {
                return CARD_ALREADY_ENABLED;
            }


            progressCallBack(15, "Lettura dati dalla CIE");
        
            ByteArray serviziData(IdServizi.left(12));

            ByteDynArray SOD;
            ias.ReadSOD(SOD);
            uint8_t digest = ias.GetSODDigestAlg(SOD);
                        
            ByteArray intAuthData(IntAuth.left(GetASN1DataLenght(IntAuth)));
            
			ByteDynArray IntAuthServizi;
            ias.ReadServiziPubKey(IntAuthServizi);
            ByteArray intAuthServiziData(IntAuthServizi.left(GetASN1DataLenght(IntAuthServizi)));

            ias.SelectAID_IAS();
            ByteDynArray DH;
            ias.ReadDH(DH);
            ByteArray dhData(DH.left(GetASN1DataLenght(DH)));

            // poichè la CIE abilitata sul desktop può essere solo una, szPAN passato da CIEID è sempre null
//            if (szPAN && IdServizi != ByteArray((uint8_t*)szPAN, strnlen(szPAN, 20)))
//                continue;

            foundCIE = true;
            
            progressCallBack(20, "Autenticazione...");
            
            free(readers);
            readers = NULL;
            free(ATR);
            ATR = NULL;

            DWORD rs = CardAuthenticateEx(&ias, ROLE_USER, FULL_PIN, (BYTE*)szPIN, (DWORD)strnlen(szPIN, sizeof(szPIN)), nullptr, 0, progressCallBack, attempts);
            if (rs == SCARD_W_WRONG_CHV)
            {
                return CKR_PIN_INCORRECT;
            }
            else if (rs == SCARD_W_CHV_BLOCKED)
            {
                return CKR_PIN_LOCKED;
            }
            else if (rs != SCARD_S_SUCCESS)
            {
                return CKR_GENERAL_ERROR;
            }
            
            
            progressCallBack(45, "Lettura seriale");
            
            ByteDynArray Serial;
            ias.ReadSerialeCIE(Serial);
            ByteArray serialData = Serial.left(9);
            std::string st_serial((char*)serialData.data(), serialData.size());
            printf("\nserial data: %s\n", st_serial.c_str());

            
            progressCallBack(55, "Lettura certificato");
            
            ByteDynArray CertCIE;
            ias.ReadCertCIE(CertCIE);
            ByteArray certCIEData = CertCIE.left(GetASN1DataLenght(CertCIE));
            
            if (digest == 1)
            {
                CSHA256 sha256;
                hashSet[0xa1] = sha256.Digest(serviziData);
                hashSet[0xa4] = sha256.Digest(intAuthData);
                hashSet[0xa5] = sha256.Digest(intAuthServiziData);
                hashSet[0x1b] = sha256.Digest(dhData);
                hashSet[0xa2] = sha256.Digest(serialData);
                hashSet[0xa3] = sha256.Digest(certCIEData);
                ias.VerificaSOD(SOD, hashSet);

            }
            else
            {
                CSHA512 sha512;
                hashSet[0xa1] = sha512.Digest(serviziData);
                hashSet[0xa4] = sha512.Digest(intAuthData);
                hashSet[0xa5] = sha512.Digest(intAuthServiziData);
                hashSet[0x1b] = sha512.Digest(dhData);
                hashSet[0xa2] = sha512.Digest(serialData);
                hashSet[0xa3] = sha512.Digest(certCIEData);
                ias.VerificaSODPSS(SOD, hashSet);
            }

            ByteArray pinBa((uint8_t*)szPIN, 4);
            
            progressCallBack(85, "Memorizzazione in cache");
            
            std::string sidServizi((char*)IdServizi.data(), IdServizi.size());

            ias.SetCache((char*)sidServizi.c_str(), CertCIE, pinBa);
            
            std::string span((char*)sidServizi.c_str());
            std::string name;
            std::string surname;
            
            CryptoPP::ByteQueue certin;
            certin.Put(CertCIE.data(),CertCIE.size());
            
            std::string serial;
            CryptoPP::ByteQueue issuer;
            CryptoPP::ByteQueue subject;
            std::string notBefore;
            std::string notAfter;
            CryptoPP::Integer mod;
            CryptoPP::Integer pubExp;
            
            GetCertInfo(certin, serial, issuer, subject, notBefore, notAfter, mod, pubExp);
            
            CryptoPP::BERSequenceDecoder subjectEncoder(subject);
            {
                while(!subjectEncoder.EndReached())
                {
                    CryptoPP::BERSetDecoder item(subjectEncoder);
                    CryptoPP::BERSequenceDecoder attributes(item); {
                        
                        OID oid(attributes);
                        if(oid == OID_GIVENNAME)
                        {
                            byte tag = 0;
                            attributes.Peek(tag);
                            
                            CryptoPP::BERDecodeTextString(
                                                          attributes,
                                                          name,
                                                          tag);
                        }
                        else if(oid == OID_SURNAME)
                        {
                            byte tag = 0;
                            attributes.Peek(tag);
                            
                            CryptoPP::BERDecodeTextString(
                                                          attributes,
                                                          surname,
                                                          tag);
                        }
                        
                        item.SkipAll();
                    }
                }
            }
        
            subjectEncoder.SkipAll();
            
            std::string fullname = name + " " + surname;
            completedCallBack(span.c_str(), fullname.c_str(), st_serial.c_str());
		}
        
		if (!foundCIE) {
            return CKR_TOKEN_NOT_RECOGNIZED;
            
		}

	}
	catch (std::exception &ex) {
		OutputDebugString(ex.what());
        if(ATR)
            free(ATR);
        
        if(readers)
            free(readers);
        return CKR_GENERAL_ERROR;
	}

    if(ATR)
        free(ATR);
    if(readers)
    	free(readers);
    
    progressCallBack(100, "");
    
    return SCARD_S_SUCCESS;
}



DWORD CardAuthenticateEx(IAS*       ias,
                         DWORD       PinId,
                         DWORD       dwFlags,
                         BYTE*       pbPinData,
                         DWORD       cbPinData,
                         BYTE*       *ppbSessionPin,
                         DWORD*      pcbSessionPin,
						 PROGRESS_CALLBACK progressCallBack,
                         int*      pcAttemptsRemaining) {
    
	progressCallBack(21, "selected CIE applet");
    ias->SelectAID_IAS();
    ias->SelectAID_CIE();
    
    

    progressCallBack(22, "init DH Param");
    // leggo i parametri di dominio DH e della chiave di extauth
    ias->InitDHParam();
    

    progressCallBack(24, "read DappPubKey");

    ByteDynArray dappData;
    ias->ReadDappPubKey(dappData);
    
    progressCallBack(26, "InitExtAuthKeyParam");
    ias->InitExtAuthKeyParam();
    
    progressCallBack(28, "DHKeyExchange");
    ias->DHKeyExchange();

    progressCallBack(30, "DAPP");

    // DAPP
    ias->DAPP();
    
    progressCallBack(32, "VerifyPIN");

    // verifica PIN
    StatusWord sw;
    if (PinId == ROLE_USER) {
        
        ByteDynArray PIN;
        if ((dwFlags & FULL_PIN) != FULL_PIN)
            ias->GetFirstPIN(PIN);
        PIN.append(ByteArray(pbPinData, cbPinData));
        sw = ias->VerifyPIN(PIN);
    }
    else if (PinId == ROLE_ADMIN) {
        ByteArray pinBa(pbPinData, cbPinData);
        sw = ias->VerifyPUK(pinBa);
    }
    else
        return SCARD_E_INVALID_PARAMETER;
    
    progressCallBack(34, "verifyPIN ok");

    if (sw == 0x6983) {
        if (PinId == ROLE_USER)
        {
            progressCallBack(40, "PIN Bloccato");
            ias->IconaSbloccoPIN();
        }

        return SCARD_W_CHV_BLOCKED;
    }
    else if (sw >= 0x63C0 && sw <= 0x63CF) {
        progressCallBack(40, "PIN Errato");

        if (pcAttemptsRemaining!=nullptr)
            *pcAttemptsRemaining = sw - 0x63C0;
        return SCARD_W_WRONG_CHV;
    }
    else if (sw == 0x6700) {
    	progressCallBack(40, "PIN Errato");
        return SCARD_W_WRONG_CHV;
    }
    else if (sw == 0x6300)
    {
    	progressCallBack(40, "PIN Errato");
        return SCARD_W_WRONG_CHV;
    }
    else if (sw != 0x9000) {
    	progressCallBack(40, "Errore smart card");
        throw scard_error(sw);
    }
    
    progressCallBack(38, "VerifyPIN OK");

    return SCARD_S_SUCCESS;
}

int TokenTransmitCallback(safeConnection *conn, BYTE *apdu, DWORD apduSize, BYTE *resp, DWORD *respSize) {
    if (apduSize == 2) {
        WORD code = *(WORD*)apdu;
        if (code == 0xfffd) {
            long bufLen = *respSize;
            *respSize = sizeof(conn->hCard)+2;
            CryptoPP::memcpy_s(resp, bufLen, &conn->hCard, sizeof(conn->hCard));
            resp[sizeof(&conn->hCard)] = 0;
            resp[sizeof(&conn->hCard) + 1] = 0;
            
            return SCARD_S_SUCCESS;
        }
        else if (code == 0xfffe) {
            DWORD protocol = 0;
            ODS("UNPOWER CARD");
            auto ris = SCardReconnect(conn->hCard, SCARD_SHARE_SHARED, SCARD_PROTOCOL_Tx, SCARD_UNPOWER_CARD, &protocol);
            
            
            if (ris == SCARD_S_SUCCESS) {
                SCardBeginTransaction(conn->hCard);
                *respSize = 2;
                resp[0] = 0x90;
                resp[1] = 0x00;
            }
            return ris;
        }
        else if (code == 0xffff) {
            DWORD protocol = 0;
            auto ris = SCardReconnect(conn->hCard, SCARD_SHARE_SHARED, SCARD_PROTOCOL_Tx, SCARD_RESET_CARD, &protocol);
            if (ris == SCARD_S_SUCCESS) {
                SCardBeginTransaction(conn->hCard);
                *respSize = 2;
                resp[0] = 0x90;
                resp[1] = 0x00;
            }
            ODS("RESET CARD");
            return ris;
        }
    }
    //ODS(String().printf("APDU: %s\n", dumpHexData(ByteArray(apdu, apduSize), String()).lock()).lock());
    auto ris = SCardTransmit(conn->hCard, SCARD_PCI_T1, apdu, apduSize, NULL, resp, respSize);
    if(ris == SCARD_W_RESET_CARD || ris == SCARD_W_UNPOWERED_CARD)
    {
        ODS("card resetted");
        DWORD protocol = 0;
        ris = SCardReconnect(conn->hCard, SCARD_SHARE_SHARED, SCARD_PROTOCOL_Tx, SCARD_LEAVE_CARD, &protocol);
        if (ris != SCARD_S_SUCCESS)
            ODS("Errore reconnect");
        else
            ris = SCardTransmit(conn->hCard, SCARD_PCI_T1, apdu, apduSize, NULL, resp, respSize);
    }
    
    if (ris != SCARD_S_SUCCESS) {
        ODS("Errore trasmissione APDU");
    }
    
    //else
    //ODS(String().printf("RESP: %s\n", dumpHexData(ByteArray(resp, *respSize), String()).lock()).lock());
    
    return ris;
}



std::vector<word32> fromObjectIdentifier(std::string sObjId)
{
    std::vector<word32> out;
    
    int nVal;
    int nAux;
    char* szTok;
    char* szOID = new char[sObjId.size() + 1];
    strncpy(szOID, sObjId.c_str(), sObjId.size());
    char *next = NULL;
    szTok = strtok_r(szOID, ".", &next);

    UINT nFirst = 40 * strtol(szTok, NULL, 10) + strtol(strtok_r(NULL, ".", &next), NULL, 10);
    if(nFirst > 0xff)
    {
        delete[] szOID;
        throw -1;//new CASN1BadObjectIdException(strObjId);
    }
    
    out.push_back(nFirst);
    
    int i = 0;
    
    while ((szTok = strtok_r(NULL, ".", &next)) != NULL)
    {
        nVal = strtol(szTok, NULL, 10);
        if(nVal == 0)
        {
            out.push_back(0x00);
        }
        else if (nVal == 1)
        {
            out.push_back(0x01);
        }
        else
        {
            i = (int)ceil((log((double)abs(nVal)) / log((double)2)) / 7); // base 128
            while (nVal != 0)
            {
                nAux = (int)(floor(nVal / pow(128, i - 1)));
                nVal = nVal - (int)(pow(128, i - 1) * nAux);
                
                // next value (or with 0x80)
                if(nVal != 0)
                    nAux |= 0x80;

                out.push_back(nAux);
                
                i--;
            }
        }
    }
    
    
    delete[] szOID;

    return out;
}
bool file_exists (const char* name);

char command[1000];

void* mythread(void* thr_data) {

	char* command = (char*)thr_data;
	system(command);

	return NULL;
}

int sendMessage(const char* szCommand, const char* szParam)
{
	char* file = "/usr/share/CIEID/jre/bin/java";

	if(!file_exists(file))
		file = "java";

	const char* arg = "-Xms1G -Xmx1G -Djna.library.path=\".:/usr/local/lib\" -classpath \"/usr/share/CIEID/cieid.jar\" it.ipzs.cieid.MainApplication";

	snprintf(command, 1000, "%s %s %s", file, arg, szCommand);

	pthread_t thr;
	pthread_create(&thr, NULL, mythread, (void*)command);

	return 0;
}

//int sendMessageOld(const char* szCommand, const char* szParam)
//{
//    int sock;
//    struct sockaddr_in server;
//    char szMessage[100] , szServerReply[1000];
//
//    //Create socket
//    sock = socket(AF_INET , SOCK_STREAM , 0);
//    if (sock == -1)
//    {
//        printf("Could not create socket");
//    }
//    puts("Socket created");
//
//    server.sin_addr.s_addr = inet_addr("127.0.0.1");
//    server.sin_family = AF_INET;
//    server.sin_port = htons( 8888 );
//
//    //Connect to remote server
//    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0)
//    {
//        perror("connect failed. Error");
//        return 1;
//    }
//
//    puts("Connected\n");
//
//    if(szParam)
//        sprintf(szMessage, "%s:%s", szCommand, szParam);
//    else
//        sprintf(szMessage, "%s", szCommand);
//
//    std::string sMessage = szMessage;
//    std::string sCipherText;
//
//    encrypt(sMessage, sCipherText);
//
//    int messagelen = (int)sCipherText.size();
//    std::string sHeader((char*)&messagelen, sizeof(messagelen));
//
//    sMessage = sHeader.append(sCipherText);
//
//    //Send some data
//    if( send(sock , sMessage.c_str(), (size_t)sMessage.length() , 0) < 0)
//    {
//        puts("Send failed");
//        return 2;
//    }
//
//    //Receive a reply from the server
//    if( recv(sock , szServerReply , 100 , 0) < 0)
//    {
//        puts("recv failed");
//        return 3;
//    }
//
//    puts("Server reply :");
//    puts(szServerReply);
//
//    close(sock);
//
//    return 0;
//}

void notifyPINLocked()
{
    sendMessage("pinlocked", NULL);
}

void notifyPINWrong(int trials)
{
    char szParam[3];
    snprintf(szParam, 3, "%d", trials);

    sendMessage("pinwrong", szParam);
}

void notifyCardNotRegistered(const char* szPAN)
{
    sendMessage("cardnotregistered", szPAN);
}
