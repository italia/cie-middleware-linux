#pragma once

#ifdef _WIN32
#pragma pack(1)
#endif
#include "pkcs11.h"
#ifdef _WIN32
#pragma pack()
#endif

#include <winscard.h>

#define MAXVAL 0xffffff
#define MAXSESSIONS MAXVAL

#ifdef _WIN32
#define CK_ENTRY __declspec(dllexport)
#else
#define CK_ENTRY __attribute__ ((visibility ("default")))
#endif
#define LIBRARY_VERSION_MAJOR 2
#define LIBRARY_VERSION_MINOR 0

#define PIN_LEN 8
#define USER_PIN_ID 0x10

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

extern "C" {
	CK_RV CK_ENTRY C_UpdateSlotList();
}
