#pragma once

#include <thread>
#include "util.h"


class CThread
{
//public:
	CThread(void);
	~CThread(void);
#ifdef WIN32
	HANDLE hThread;
#else
	std::thread hThread;
#endif
	DWORD dwThreadID;
	void createThread(void *threadFunc,void *threadData);
	DWORD joinThread(DWORD timeout);
	void terminateThread();
	void exitThread(DWORD dwCode);
	void close();
	inline static std::thread::id getID() {return std::this_thread::get_id();}
};
