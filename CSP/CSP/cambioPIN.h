#ifndef __SBLOCCO_PON_H__
#define __SBLOCCO_PON_H__

#include "../Util/defines.h"

#ifdef wIN32
#ifdef _WIN64
#pragma comment(linker, "/export:SbloccoPIN")
#else
#pragma comment(linker, "/export:SbloccoPIN=_SbloccoPIN@16")
#endif

#else
extern "C" int CALLBACK CambioPIN( /*_In_*/ HINSTANCE hInstance, /*_In_*/ HINSTANCE hPrevInstance, /*_In_*/ LPCSTR     lpCmdLine, /*_In_*/ int       nCmdShow);
#endif

#endif
