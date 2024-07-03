//
//  VerificaConCIE.h
//  cie-pkcs11
//
//  Copyright © 2021 IPZS. All rights reserved.
//

#ifndef VerificaConCIE_h
#define VerificaConCIE_h

#include <stdio.h>
#include "AbilitaCIE.h"
#include "../Sign/CIEVerify.h"

typedef CK_RV (*verificaConCIEfn)(const char* inFilePath);
typedef CK_RV (*getNumberOfSignfn)(void);
typedef CK_RV (*getVerifyInfofn)(int index, struct verifyInfo_t* vInfos);
typedef CK_RV (*estraiP7mfn)(const char* inFilePath, const char* outFilePath);
#endif /* VerificaConCIE_h */
