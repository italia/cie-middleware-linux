#pragma once

#ifdef _WIN32
#include <windows.h>
#include <atlbase.h>
#include <atlhost.h>
#include <atlstr.h>
#endif
#include <stdint.h> 
#include <winscard.h>
#include "Util/defines.h"
#include "Util/Array.h"
#include "Util/log.h"
#include "Util/funccallinfo.h"
#include "Util/util.h"
#include "Util/UtilException.h"

#ifdef _DEBUG
#define ODS(s) OutputDebugString(s)
#else
#define ODS(s)
#endif

extern ByteArray baNXP_ATR;
extern ByteArray baGemalto_ATR;
extern ByteArray baGemalto2_ATR;
