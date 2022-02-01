#include "wintypes.h"
#include "../Util/util.h"
#include "CardContext.h"

extern CLog Log;

void CCardContext::getContext() {
	init_func
#ifdef WIN32
	HANDLE hSCardSystemEvent=SCardAccessStartedEvent();
	if (hSCardSystemEvent) {
		WaitForSingleObject(hSCardSystemEvent,INFINITE);
		SCardReleaseStartedEvent();
	}
#endif
    
	LONG _call_ris;
	if ((_call_ris=(SCardEstablishContext(SCARD_SCOPE_USER,NULL,NULL,&hContext)))!=S_OK) {
		throw windows_error(_call_ris);
	}
}

CCardContext::CCardContext(void)
{
	hContext=NULL;
	getContext();
}

CCardContext::~CCardContext(void)
{
	if (hContext)
		SCardReleaseContext(hContext);
}

CCardContext::operator SCARDCONTEXT() {
	return hContext;
}


void CCardContext::validate() {

#ifdef WIN32
	HANDLE hSCardSystemEvent=SCardAccessStartedEvent();
	if (hSCardSystemEvent) {
		WaitForSingleObject(hSCardSystemEvent,INFINITE);
		SCardReleaseStartedEvent();
	}
#endif
    
	if (hContext)
		if (SCardIsValidContext(hContext)!=SCARD_S_SUCCESS) 
			hContext = NULL;

	if (hContext == 0) {
		getContext();
	}
}

void CCardContext::renew() {
	init_func
	
	LONG ris;
	if (hContext)
		if ((ris=SCardReleaseContext(hContext)) != SCARD_S_SUCCESS)
			throw windows_error(ris);
	hContext=NULL;

	getContext();

}
