#include "../StdAfx.h"
#ifdef _WIN32
#include <windows.h>
#include <imagehlp.h>
#endif
#include "UtilException.h"
#include <stdio.h>

extern thread_local std::unique_ptr<CFuncCallInfoList> callQueue;

logged_error::logged_error(const char *message) : std::runtime_error(message) {
	OutputDebugString(what());
	CFuncCallInfoList *ptr = callQueue.get();
	while (ptr != nullptr) {
		OutputDebugString(ptr->info->FunctionName());
		ptr = ptr->next.get();
	}
}

scard_error::scard_error(StatusWord sw) : logged_error(stdPrintf("Errore smart card:%04x", sw)) { }

windows_error::windows_error(long ris) : logged_error(stdPrintf("Errore windows:%s (%08x) ", 
#ifdef _WIN32
			WinErr(ris), 
#else
			"",
#endif
			ris)) {}
