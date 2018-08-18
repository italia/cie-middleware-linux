#pragma once

#ifndef _WIN32
#include <mutex>
#endif
#include "util.h"
#include "UtilException.h"

class CSyncroMutex
{
#ifdef _WIN32
	HANDLE hMutex;
#else
	std::mutex hMutex;
#endif
public:
	void Create(void);

	CSyncroMutex(void);
	void Create(const char *name);
	~CSyncroMutex(void);

	void Lock();
	void Unlock();
};

class CSyncroLocker
{
	CSyncroMutex *pMutex;
public:
	CSyncroLocker(CSyncroMutex &mutex);
	~CSyncroLocker();
};
