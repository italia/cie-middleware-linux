//
//  AbilitaCIE.h
//  cie-pkcs11
//
//  Created by ugo chirico on 02/09/18. http://www.ugochirico.com
//  Copyright © 2018 IPZS. All rights reserved.
//
#include "../PKCS11/cryptoki.h"
#include <string>
#include "../PCSC/PCSC.h"

#define SCARD_ATTR_VALUE(Class, Tag) ((((uint32_t)(Class)) << 16) | ((uint32_t)(Tag)))
#define SCARD_CLASS_ICC_STATE       9   /**< ICC State specific definitions */
#define SCARD_ATTR_ATR_STRING SCARD_ATTR_VALUE(SCARD_CLASS_ICC_STATE, 0x0303) /**< Answer to reset (ATR) string. */

//using namespace std

/* CK_NOTIFY is an application callback that processes events */
typedef CK_CALLBACK_FUNCTION(CK_RV, PROGRESS_CALLBACK)(
                                               const int progress,
                                               const char* szMessage);

typedef CK_CALLBACK_FUNCTION(CK_RV, COMPLETED_CALLBACK)(
                                                        const char* szPan,
                                                        const char* szName,
														const char* ef_seriale);

typedef CK_RV (*AbilitaCIEfn)(const char*  szPAN,
                              const char*  szPIN,
                              int* attempts,
                              PROGRESS_CALLBACK progressCallBack,
                              COMPLETED_CALLBACK completedCallBack);

typedef CK_RV (*VerificaCIEAbilitatafn)();
typedef CK_RV (*DisabilitaCIEfn)();

int TokenTransmitCallback(safeConnection *data, uint8_t *apdu, DWORD apduSize, uint8_t *resp, DWORD *respSize);


