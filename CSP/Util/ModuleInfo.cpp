#include "../StdAfx.h"
#include "ModuleInfo.h"
#include "UtilException.h"

static const char *szCompiledFile=__FILE__;

CModuleInfo::CModuleInfo()
{
}

HANDLE CModuleInfo::getApplicationModule()
{
#ifdef _WIN32
	return (HANDLE)GetModuleHandle(NULL);
#else
	return nullptr;
#endif
}

HANDLE CModuleInfo::getModule() {
	return module;
}

void CModuleInfo::init(HANDLE module)
{
	this->module = module;
#ifdef _WIN32
	char path[MAX_PATH];
	if (GetModuleFileName((HMODULE)module,path,MAX_PATH)==0)
		throw windows_error(GetLastError());
	
	szModuleFullPath=path;

	char drive[_MAX_DRIVE], dir[_MAX_DIR],fname[_MAX_FNAME],ext[_MAX_EXT];
	_splitpath_s(szModuleFullPath.c_str(), drive, dir, fname, ext);
	szModuleName = fname;
	char moddir[MAX_PATH];
	_makepath_s( moddir,drive,dir,NULL,NULL);
	szModulePath = moddir;
#endif
}

CModuleInfo::~CModuleInfo(void)
{
}
