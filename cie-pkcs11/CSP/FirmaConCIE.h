//
//  FirmaConCIE.hpp
//  cie-pkcs11
//
//  Copyright © 2021 IPZS. All rights reserved.
//

#ifndef FirmaConCIE_h
#define FirmaConCIE_h

#include <stdio.h>
#include <string>
#include "../PKCS11/cryptoki.h"
#include "../Sign/CIESign.h"
#include "AbilitaCIE.h"

typedef CK_CALLBACK_FUNCTION(CK_RV, SIGN_COMPLETED_CALLBACK)(
                                               const int ret);


typedef CK_RV (*firmaConCIEfn)(const char* inFilePath, const char* type, const char* pin, const char* pan, int page, float x, float y, float w, float h, const char* imagePathFile, const char* outFilePath, PROGRESS_CALLBACK progressCallBack, SIGN_COMPLETED_CALLBACK completedCallBack);

#endif /* FirmaConCIE_h */
