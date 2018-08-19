#include "StdAfx.h"

#include "Util/ModuleInfo.h"

CModuleInfo moduleInfo;

#ifdef _WIN32

BOOL APIENTRY DllMainP11(HANDLE hModule, DWORD  ul_reason_for_call, LPVOID lpReserved);

BOOL WINAPI DllMain(
	_In_ HINSTANCE hinstDLL,
	_In_ DWORD     fdwReason,
	_In_ LPVOID    lpvReserved
	) {
	switch (fdwReason)
	{
	case DLL_PROCESS_ATTACH:
		moduleInfo.init(hinstDLL);
		break;
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
	if (!DllMainP11(hinstDLL, fdwReason, lpvReserved))
		return FALSE;
	return TRUE;
}
#endif
