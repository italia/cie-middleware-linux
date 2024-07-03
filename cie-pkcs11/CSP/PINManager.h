//
//  PINManager.hpp
//  cie-pkcs11
//
//  Created by ugo chirico on 06/10/18. http://www.ugochirico.com
//  SPDX-License-Identifier: BSD-3-Clause
//

#ifndef PINManager_h
#define PINManager_h

#include <stdio.h>
#include "AbilitaCIE.h"

typedef CK_RV (*CambioPINfn)(const char*  szCurrentPIN,
                            const char*  szNewPIN,
                            int* attempts,
                            PROGRESS_CALLBACK progressCallBack);

typedef CK_RV (*SbloccoPINfn)(const char*  szPUK,
                            const char*  szNewPIN,
                            int* attempts,
                            PROGRESS_CALLBACK progressCallBack);

#endif /* PINManager_h */
