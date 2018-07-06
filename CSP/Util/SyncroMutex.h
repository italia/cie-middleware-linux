#pragma once

#include <mutex>
#include "util.h"
#include "UtilException.h"

class CSyncroMutex
{
	std::mutex hMutex;
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
