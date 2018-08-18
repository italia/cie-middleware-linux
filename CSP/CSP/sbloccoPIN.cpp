#include "../StdAfx.h"
#include <winscard.h>
#include "../PCSC/PCSC.h"
#include <reader.h>
#include "IAS.h"
#include "CSP.h"
#include "../Util/ModuleInfo.h"
#include "../UI/Message.h"
#include "../UI/PIN.h"
#include <functional>
#include "../Crypto/ASNParser.h"
#include "CardMod.h"
#include "../UI/SystemTray.h"
#include "../UI/safeDesktop.h"
#include <string>
#ifdef __linux__
	#include "../Util/defines.h"
	#include "../Util/funccallinfo.h"
	#include "helper.h"	
#elif defined(_WIN32)
	#include <atlbase.h>
#endif
#include "sbloccoPIN.h"

extern CModuleInfo moduleInfo;
extern "C" DWORD WINAPI CardAcquireContext(IN PCARD_DATA pCardData, __in DWORD dwFlags);

#ifdef _WIN64
#pragma comment(linker, "/export:SbloccoPIN")
#else
#pragma comment(linker, "/export:SbloccoPIN=_SbloccoPIN@16")
#endif

DWORD WINAPI _sbloccoPIN(
	DWORD threadId) {
	init_func

		try {
		DWORD len = 0;
		ByteDynArray IdServizi;
		std::unique_ptr<safeDesktop> desk;

		SCARDCONTEXT hSC;

		SCardEstablishContext(SCARD_SCOPE_SYSTEM, nullptr, nullptr, &hSC);
		char *readers = nullptr;
		len = SCARD_AUTOALLOCATE;
		if (SCardListReaders(hSC, nullptr, (char*)&readers, &len) != SCARD_S_SUCCESS || readers == nullptr) {
			CMessage msg(MB_OK,
				"Sblocco PIN",
				"Nessun lettore di smartcard installato");
			msg.DoModal();
			return 0;
		}
		char *curreader = readers;
		bool foundCIE = false, isEnrolled = false;
		for (; curreader[0] != 0; curreader += strnlen(curreader, len) + 1) {
			CARD_DATA cData;
			ZeroMem(cData);
			cData.dwVersion = 7;
			cData.hSCardCtx = hSC;
			{
				safeConnection conn(hSC, curreader, SCARD_SHARE_SHARED);
				if (conn.hCard == NULL)
					continue;


				{
					safeTransaction checkTran(conn, SCARD_LEAVE_CARD);
					if (!checkTran.isLocked())
						continue;

					len = SCARD_AUTOALLOCATE;
					cData.hScard = conn;
					SCardGetAttrib(cData.hScard, SCARD_ATTR_ATR_STRING, (BYTE*)&cData.pbAtr, &len);
				      #ifdef _WIN32
					cData.pfnCspAlloc = (PFN_CSP_ALLOC)CryptMemAlloc;
					cData.pfnCspReAlloc = (PFN_CSP_REALLOC)CryptMemRealloc;
					cData.pfnCspFree = (PFN_CSP_FREE)CryptMemFree;
				      #elif defined(__linux__)
					cData.pfnCspAlloc = (PFN_CSP_ALLOC)malloc;//CryptMemAlloc;	//TODO: are there more secure alternatives?
					cData.pfnCspReAlloc = (PFN_CSP_REALLOC)realloc;//CryptMemRealloc;
					cData.pfnCspFree = (PFN_CSP_FREE)free;//CryptMemFree;
				      #endif
					cData.cbAtr = len;
					cData.pwszCardName = L"CIE";
					auto isCIE = CardAcquireContext(&cData, 0);
					SCardFreeMemory(cData.hScard, cData.pbAtr);
					isEnrolled = (((IAS*)cData.pvVendorSpecific)->IsEnrolled());						
					if (isCIE != 0)
						continue;
				}
				foundCIE = true;
				if (!desk)
					desk.reset(new safeDesktop("AbilitaCIE"));

				CPin puk(8, "Inserire le 8 cifre del PUK della CIE", "", "", "Sblocco PIN");
				if (puk.DoModal() == IDOK) {
					int numCifre = 8;
					std::string msg;
					msg = "Inserire le 8 cifre del nuovo PIN";

					CPin newPin(numCifre, msg.c_str(), "", "", "Sblocco PIN", true);
					if (newPin.DoModal() == IDOK) {
						try {

							safeTransaction Tran(conn, SCARD_LEAVE_CARD);
							if (!Tran.isLocked())
								continue;

							len = 0;
							auto ias = ((IAS*)cData.pvVendorSpecific);

							auto ris = CardUnblockPin(&cData, wszCARD_USER_USER, (BYTE*)puk.PIN, (DWORD)strnlen(puk.PIN, sizeof(puk.PIN)), (BYTE*)newPin.PIN, (DWORD)strnlen(newPin.PIN, sizeof(newPin.PIN)), 0, CARD_AUTHENTICATE_PIN_PIN);
							if (ris == SCARD_W_WRONG_CHV) {
								std::string num;
								if (ias->attemptsRemaining >= 0)
									num = "PUK errato. Sono rimasti " + std::to_string(ias->attemptsRemaining) + " tentativi";
								else
									num = "";
								CMessage msg(MB_OK, "Sblocco PIN",									
									num.c_str(),
									"prima di bloccare il PUK");
								msg.DoModal();
								if (threadId != 0) {
									#ifdef _WIN32
									PostThreadMessage(threadId, WM_COMMAND, 1, 0);
									#else
									//TODO: do something
									#endif
								}
								break;
							}
							else if (ris == SCARD_W_CHV_BLOCKED) {
								CMessage msg(MB_OK,
									"Sblocco PIN",
									"Il PUK e' bloccato. La CIE non può più essere sbloccata");
								msg.DoModal();
								if (threadId != 0) {
									#ifdef _WIN32
									PostThreadMessage(threadId, WM_COMMAND, 0, 0);
									#else
									//TODO: do something
									#endif
								}
								break;
							}
							else if (ris != 0)
								throw logged_error("Autenticazione fallita");

							CMessage msg(MB_OK, "Sblocco PIN",
								"Il PIN e' stato sbloccato correttamente");
							msg.DoModal();
							if (threadId != 0) {
								#ifdef _WIN32
								PostThreadMessage(threadId, WM_COMMAND, 0, 0);
								#else
								//TODO: do something
								#endif
							}
						}
						catch (std::exception &ex) {
							std::string dump;
							OutputDebugString(ex.what());
							CMessage msg(MB_OK, "Sblocco PIN",
								"Si e' verificato un errore nella verifica del PUK");
							msg.DoModal();
							if (threadId != 0) {
								#ifdef _WIN32
								PostThreadMessage(threadId, WM_COMMAND, 0, 0);
								#else
								//TODO: do something
								#endif
							}
							break;
						}
					}
					else if (threadId != 0) {
							#ifdef _WIN32
							PostThreadMessage(threadId, WM_COMMAND, 1, 0);
							#else
							//TODO: do something
							#endif
					}
				} else if (threadId != 0) {
						#ifdef _WIN32
						PostThreadMessage(threadId, WM_COMMAND, 1, 0);
						#else
						//TODO: do something
						#endif
				}
				break;
			}
		}
		if (!foundCIE) {
			if (!desk)
				desk.reset(new safeDesktop("AbilitaCIE"));
			CMessage msg(MB_OK, "Sblocco PIN",
				"Impossibile trovare una CIE",
				"nei lettori di smart card");
			msg.DoModal();
			if (threadId != 0) {
				#ifdef _WIN32
				PostThreadMessage(threadId, WM_COMMAND, 0, 0);
				#else
				//TODO: do something
				#endif
			}
		}
		SCardFreeMemory(hSC, readers);
	}
	catch (std::exception &ex) {				
		CMessage msg(MB_OK, "CIE", "Si e' verificato un errore nella verifica di autenticita' del documento");
		msg.DoModal();
	}

	return 0;
	exit_func
	return E_UNEXPECTED;
}

void TrayNotification(CSystemTray* tray, WPARAM uID, LPARAM lEvent) {
      #ifdef _WIN32
	if (lEvent == WM_LBUTTONUP || lEvent== 0x405) {
		std::thread thread(_sbloccoPIN, GetCurrentThreadId());
		thread.detach();
		tray->HideIcon();
	}
      #else
	//TODO: implement something?
      #endif
}

int CALLBACK SbloccoPIN(
	_In_ HINSTANCE hInstance,
	_In_ HINSTANCE hPrevInstance,
	_In_ LPCSTR     lpCmdLine,
	_In_ int       nCmdShow
	)
{
	init_CSP_func
      #ifdef _WIN32
	if (_AtlWinModule.cbSize != sizeof(_ATL_WIN_MODULE)) {
		_AtlWinModule.cbSize = sizeof(_ATL_WIN_MODULE);
		AtlWinModuleInit(&_AtlWinModule);
	}

	WNDCLASS wndClass;
	GetClassInfo(NULL, WC_DIALOG, &wndClass);
	wndClass.hInstance = (HINSTANCE)moduleInfo.getModule();
	wndClass.style |= CS_DROPSHADOW;
	wndClass.lpszClassName = "CIEDialog";
	RegisterClass(&wndClass);
      #endif

	ODS("Start SbloccoPIN");
	if (!CheckOneInstance("CIESbloccoOnce")) {
		ODS("Already running SbloccoPIN");
		return 0;
	}	

	if (strcmp(lpCmdLine, "ICON") == 0) {
	      #ifdef _WIN32
		CSystemTray tray(wndClass.hInstance, nullptr, WM_APP, "Premere per sbloccare il PIN della CIE",
			LoadIcon(wndClass.hInstance, MAKEINTRESOURCE(IDI_CIE)), 1);
		tray.ShowBalloon("Premere per sbloccare il PIN dalla CIE", "CIE", NIIF_INFO);
		tray.ShowIcon();
		tray.TrayNotification = TrayNotification;
		MSG Msg;
		while (GetMessage(&Msg, NULL, 0, 0) > 0)
		{
			TranslateMessage(&Msg);
			if (Msg.message == WM_COMMAND) {
				if (Msg.wParam == 0)
					return 0;
				else {
					tray.ShowIcon();
					tray.ShowBalloon("Premere per sbloccare il PIN dalla CIE", "CIE", NIIF_INFO);
				}
			}
			DispatchMessage(&Msg);
		}
	      #else
		std::cout << __FILE__ <<  " Premere per sbloccare il PIN della CIE" << std::endl;	//TODO: implement something?
	      #endif
	}
	else {
		//std::thread thread(_sbloccoPIN, 0);
		//thread.join();
		_sbloccoPIN(0);

		ODS("End SbloccoPIN");
		return 0;
	}
	exit_CSP_func
	return 0;
}
