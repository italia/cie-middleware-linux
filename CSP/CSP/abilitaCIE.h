#ifndef __ABILITA_CIE_H__
#define __ABILITA_CIE_H__

#include "../Util/defines.h"

#ifdef WIN32
#ifdef _WIN64
	#pragma comment(linker, "/export:AbilitaCIE")
#else
	#pragma comment(linker, "/export:AbilitaCIE=_AbilitaCIE@16")
#endif

#else
extern "C" int CALLBACK AbilitaCIE( /*_In_*/ HINSTANCE hInstance, /*_In_*/ HINSTANCE hPrevInstance, /*_In_*/ LPCSTR lpCmdLine, /*_In_*/ int       nCmdShow);
#endif

#endif
