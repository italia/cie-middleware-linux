#include "../StdAfx.h"
#include "Thread.h"
#include "util.h"
#include "UtilException.h"

static const char *szCompiledFile=__FILE__;

CThread::CThread(void)
{
#ifdef WIN32
	hThread=NULL;
#endif
	dwThreadID=0;
}

CThread::~CThread(void)
{
	dwThreadID=0;
#ifdef WIN32
	if (hThread)
		CloseHandle(hThread);
#endif
}

void CThread::close()
{
	dwThreadID = 0;
#ifdef WIN32
	if (hThread)
		CloseHandle(hThread);
	hThread = nullptr;
#endif
}

void CThread::createThread(void *threadFunc,void *threadData)
{
	init_func
#ifdef WIN32	
	if (dwThreadID != 0 || hThread!=nullptr)
		throw CStringException("Thread non ancora chiuso");
#else
	if (dwThreadID != 0)
		throw std::runtime_error("Thread non ancora chiuso");
#endif

#ifdef WIN32
	hThread=CreateThread(NULL,0,(LPTHREAD_START_ROUTINE)threadFunc,threadData,0,&dwThreadID);
	if (!hThread) {
		throw CWinException();
	}
#else
	//TODO: implement this! //hThread = std::thread(threadFunc, threadData);
#endif
	exit_func
}

DWORD CThread::joinThread(DWORD timeout)
{
	init_func
#ifdef WIN32
	DWORD ret=WaitForSingleObject(hThread,timeout);
	if (ret == 0)
		return OK;
	if (ret==WAIT_FAILED) {
		// verifico se il thread è stato già chiuso...
		if (GetLastError()==ERROR_INVALID_HANDLE)
			return OK;
		return FAIL;
	}
	return FAIL;
#else
	hThread.join();
	return 0;
#endif
	exit_func
}

void CThread::exitThread(DWORD dwCode)
{
	init_func
#ifdef WIN32
	if (hThread) {
		if (!CloseHandle(hThread))
			throw CWinException();
		hThread=NULL;
		ExitThread(dwCode);
		dwThreadID=0;
	}
#else
	//TODO: implement this!	//pthread_cancel(std::this_thread::native_handle());
	dwThreadID=0;
#endif
	exit_func
}

void CThread::terminateThread()
{
	init_func
	dwThreadID=0;
#ifdef WIN32
	#pragma warning(suppress: 6258)
	BOOL ris = TerminateThread(hThread, 0);
	if (!ris)
		throw CWinException();
#else
	pthread_cancel(hThread.native_handle());
#endif
	exit_func
}
