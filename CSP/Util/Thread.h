#pragma once

#include <thread>
#include "util.h"


class CThread
{
//public:
	CThread(void);
	~CThread(void);
#ifdef _WIN32
	HANDLE hThread;
	DWORD dwThreadID;
#else
	std::thread hThread;
#endif
	DWORD dwThreadID;
	void createThread(void *threadFunc,void *threadData);
	DWORD joinThread(DWORD timeout);
	void terminateThread();
	void exitThread(DWORD dwCode);
	void close();
#ifdef _WIN32
	inline static DWORD getID() {return GetCurrentThreadId();}
#else
	inline static std::thread::id getID() {return std::this_thread::get_id();}
#endif
};
