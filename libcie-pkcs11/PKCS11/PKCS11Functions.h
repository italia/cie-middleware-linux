#pragma once

#include "cryptoki.h"

#ifdef WIN32
#include <winscard.h>
#else
#include <PCSC/winscard.h>
#endif

#define MAXVAL 0xffffff
#define MAXSESSIONS MAXVAL

#ifdef WIN32
#define CK_ENTRY __declspec(dllexport)
#else
#define CK_ENTRY
#endif
#define LIBRARY_VERSION_MAJOR 2
#define LIBRARY_VERSION_MINOR 0

#define PIN_LEN 8
#define USER_PIN_ID 0x10

#ifdef WIN32

#define init_p11_func \
	CFuncCallInfo info(__FUNCTION__, Log); \
	try {

#define exit_p11_func } \
	catch (p11_error &p11Err) { \
		OutputDebugString("EXCLOG->"); \
		OutputDebugString(p11Err.what()); \
		OutputDebugString("<-EXCLOG");\
		return p11Err.getP11ErrorCode(); \
	} \
	catch (std::exception &err) { \
		OutputDebugString("EXCLOG->"); \
		OutputDebugString(err.what()); \
		OutputDebugString("<-EXCLOG");\
		return CKR_GENERAL_ERROR; \
	} \
catch (...) { return CKR_GENERAL_ERROR; }

#else

#define init_p11_func \
    CFuncCallInfo info(__FUNCTION__, Log); \
    try {

#define exit_p11_func } \
catch (p11_error &p11Err) { \
Log.write(p11Err.what()); \
OutputDebugString("EXCLOG->"); \
OutputDebugString("EXC: %s", p11Err.what()); \
OutputDebugString("<-EXCLOG");\
Log.writePure("P11Error: %x", p11Err.getP11ErrorCode()); \
return p11Err.getP11ErrorCode(); \
} \
catch (std::exception &err) { \
Log.write(err.what()); \
OutputDebugString("EXCLOG->"); \
OutputDebugString("EXC: %s", err.what()); \
OutputDebugString("<-EXCLOG");\
return CKR_GENERAL_ERROR; \
} \
catch (...) { Log.writePure("%s, CKR_GENERAL_ERROR", __FUNCTION__); return CKR_GENERAL_ERROR; }

#endif

extern "C" {
    CK_RV CK_ENTRY C_UpdateSlotList();
}
