// PinTDlg.cpp : implementazione di CPinTDlg

#include "../StdAfx.h"
#include "PIN.h"
#ifndef _WIN32
        #include "helper.h"
#endif

CPin::CPin(int PinLen, const char *message, const char *message2, const char *message3, const char *title, bool repeat)
{
#ifdef _WIN32
	txtFont = CreateFont(20, 0, 0, 0, 800, FALSE, FALSE, FALSE, ANSI_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS, 5, DEFAULT_PITCH, "Arial");
	txtFont2 = CreateFont(15, 0, 0, 0, 400, FALSE, FALSE, FALSE, ANSI_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS, 5, DEFAULT_PITCH, "Arial");
#endif
	this->message = message;
	this->message2 = message2;
	this->message3 = message3;
	this->title = title;
	this->repeat = repeat;
	this->PinLen = PinLen;
}

CPin::~CPin()
{
#ifdef _WIN32
	DeleteObject(txtFont);
	DeleteObject(txtFont2);
#endif
}

LRESULT CPin::OnBGnBrush(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled) {
#ifdef _WIN32
	bHandled = TRUE;
	SetBkMode((HDC)wParam, TRANSPARENT);
	return (INT_PTR)::GetStockObject(NULL_PEN);
#else
	return 0;
#endif
}

LRESULT CPin::OnHitTest(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled) {
#ifdef _WIN32
	bHandled = TRUE;
	return (LRESULT)HTCAPTION;
#else 
	return 0;
#endif
}

LRESULT CPin::OnCtlColor(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled)  {
#ifdef _WIN32
	bHandled = TRUE;
	int id = ::GetDlgCtrlID((HWND)lParam);
	SetBkMode((HDC)wParam, TRANSPARENT);
	return (INT_PTR)::GetStockObject(NULL_PEN);
#else 
	return 0;
#endif
}

LRESULT CPin::OnInitDialog(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
#ifdef _WIN32
	CenterWindow();
	SetIcon(LoadIcon((HINSTANCE)moduleInfo.getModule(), MAKEINTRESOURCE(IDI_CIE)));

	edit = GetDlgItem(IDC_PIN1);
	edit2 = GetDlgItem(IDC_PIN2);
	tit = GetDlgItem(IDC_TITLE);

	edit.SetFont(txtFont, FALSE);
	edit2.SetFont(txtFont, FALSE);

	edit.SendMessage(EM_SETLIMITTEXT, PinLen, 0);
	if (!repeat)
		edit.SetFocus();

	edit2.SendMessage(EM_SETLIMITTEXT, PinLen, 0);
	if (repeat)
		edit2.SetFocus();
	else
		edit2.ShowWindow(SW_HIDE);

	BOOL r = okButton.SubclassWindow(GetDlgItem(IDOK));
	r = cancelButton.SubclassWindow(GetDlgItem(IDCANCEL));
	okButton.LoadStateBitmaps(IDB_OK, IDB_OK, IDB_OK2);
	cancelButton.LoadStateBitmaps(IDB_CANCEL, IDB_CANCEL, IDB_CANCEL2);
	backGround.LoadImageResource(IDB_BACKGROUND);

	if (title != NULL) {
		tit.SetFont(txtFont, FALSE);
		tit.SetWindowTextA(title);
	}

	GetDlgItem(IDC_MSG1).SetFont(txtFont, FALSE);
	GetDlgItem(IDC_MSG1).SetWindowTextA(message);

	GetDlgItem(IDC_MSG3).SetFont(txtFont2, FALSE);
	GetDlgItem(IDC_MSG4).SetFont(txtFont2, FALSE);
	GetDlgItem(IDC_MSG3).SetWindowTextA(message2);
	GetDlgItem(IDC_MSG4).SetWindowTextA(message3);

	return 0;  // Lo stato attivo verrà impostato da me
#else 
	return 0;
#endif
}

LRESULT CPin::OnClickedOK(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
#ifdef _WIN32
	bHandled = TRUE;
	edit.GetWindowTextA(PIN, 99);
	if (PIN[0] == 0) {
		ShowToolTip(edit, L"Inserire il PIN e premere OK", L"PIN vuoto");
		return TRUE;
	}
	if (strnlen_s(PIN, 99) != PinLen) {
		WCHAR tip[100];
		wsprintfW(tip, L"Il PIN deve essere di %i cifre", PinLen);
		ShowToolTip(edit, tip, L"Lunghezza PIN errata");
		return TRUE;
	}
	if (repeat) {
		char PIN2[100];
		edit2.GetWindowTextA(PIN2, 99);
		if (PIN2[0] == 0) {
			ShowToolTip(edit2, L"Inserire il PIN e premere OK", L"PIN vuoto");
			return TRUE;
		}
		if (strnlen_s(PIN2, 99) != PinLen) {
			WCHAR tip[100];
			wsprintfW(tip, L"Il PIN deve essere di %i cifre", PinLen);
			ShowToolTip(edit2, tip, L"Lunghezza PIN errata");
			return TRUE;
		}
		char ref = PIN2[0];
		char last;
		for (int i = 1; i < PinLen; i++) {
			last = PIN2[i];
			if (last != ref)
				break;
		}
		if (last == ref) {
			ShowToolTip(edit2, L"Il nuovo PIN non deve essere composto da cifre uguali", L"PIN non valido");
			return TRUE;
		}

		char prec = PIN2[0];
		bool isSequence = true;
		for (int i = 1; i < PinLen; i++) {
			prec++;
			if (PIN2[i] != prec) {
				isSequence = false;
				break;
			}
		}
		if (isSequence) {
			ShowToolTip(edit2, L"Il nuovo PIN non deve essere composto da cifre consecutive", L"PIN non valido");
			return TRUE;
		}

		prec = PIN2[0];
		isSequence = true;
		for (int i = 1; i < PinLen; i++) {
			prec--;
			if (PIN2[i] != prec) {
				isSequence = false;
				break;
			}
		}
		if (isSequence) {
			ShowToolTip(edit2, L"Il nuovo PIN non deve essere composto da cifre consecutive", L"PIN non valido");
			return TRUE;
		}


		if (StrCmpN(PIN, PIN2, 99) != 0) {
			ShowToolTip(edit, L"Il PIN deve essere digitato due volte", L"PIN non corrispondente");
			return TRUE;
		}
	}
	EndDialog(IDOK);
	return TRUE;
#else 
	return 0;
#endif
}

LRESULT CPin::OnClickedCancel(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
#ifdef _WIN32
	bHandled = TRUE;
	EndDialog(wID);
	return TRUE;
#else 
	return 0;
#endif
}

LRESULT CPin::OnPaint(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
#ifdef _WIN32
	RECT rect;
	ZeroMem(rect);
	GetUpdateRect(&rect);
	PAINTSTRUCT ps;
	BeginPaint(&ps);
	backGround.Attach(ps.hdc);
	backGround.DrawBitmap(0, 0);
	backGround.Detach();

	EndPaint(&ps);

	bHandled = TRUE;
	return 1;
#else 
	return 0;
#endif
}

#ifdef _WIN32
void CPin::ShowToolTip(CEdit &edit, WCHAR *msg, WCHAR *title) {
	EDITBALLOONTIP ebt;

	ebt.cbStruct = sizeof(EDITBALLOONTIP);
	ebt.pszText = msg;
	ebt.pszTitle = title;
	ebt.ttiIcon = TTI_ERROR;    // tooltip icon

	SendMessage(edit.m_hWnd, EM_SHOWBALLOONTIP, 0, (LPARAM)&ebt);
}
#endif


#ifndef _WIN32
INT_PTR CPin::DoModal()
{
#if 0
        if(fgets(PIN, sizeof(PIN), stdin))
                PIN[strcspn(PIN, "\n")] = '\0';
        return IDOK;
#else
	bool again;
	INT_PTR ret;
        char PIN2[100];

        do {
                again = false;
                if(!repeat)
                        ret = UIhelper::makePinDialog(PinLen, message, message2, message3, title, PIN);
                else ret = UIhelper::makeDualPinDialog(PinLen, message, message2, message3, title, PIN, PIN2);

                if(ret == 1)
                {
                        if (PIN[0] == 0) {
				UIhelper::showPassiveMessage("Inserire il PIN e premere OK", "PIN vuoto");
                                again = true;
                        }
                        else if (strnlen(PIN, 99) != PinLen) {
                                char tip[100];
                                sprintf(tip, "Il PIN deve essere di %i cifre", PinLen);
				UIhelper::showPassiveMessage(tip, "Lunghezza PIN errata");
                                again = true;
                        }
                        else if (repeat) {
                                if (PIN2[0] == 0) {
					UIhelper::showPassiveMessage("Inserire il PIN e premere OK", "PIN vuoto");
                                        again = true;
                                }
                                else if (strnlen(PIN2, 99) != PinLen) {
                                        char tip[100];
                                        sprintf(tip, "Il PIN deve essere di %i cifre", PinLen);
					UIhelper::showPassiveMessage(tip, "Lunghezza PIN errata");
                                        again = true;
                                }
                                else if (strncmp(PIN, PIN2, 99) != 0) {
					UIhelper::showPassiveMessage("Il PIN deve essere digitato due volte", "PIN non corrispondente");
                                        again = true;
                                }
				else if(!again) {
					char ref = PIN2[0];
					char last;
					for (int i = 1; i < PinLen; i++) {
						last = PIN2[i];
						if (last != ref)
							break;
					}
					if (last == ref) {
						UIhelper::showPassiveMessage("Il nuovo PIN non deve essere composto da cifre uguali", "PIN non valido");
                                        	again = true;
					}
			
					char prec = PIN2[0];
					bool isSequence = true;
					for (int i = 1; i < PinLen; i++) {
						prec++;
						if (PIN2[i] != prec) {
							isSequence = false;
							break;
						}
					}
					if (isSequence && !again) {
						UIhelper::showPassiveMessage("Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido");
                                        	again = true;
					}
			
					prec = PIN2[0];
					isSequence = true;
					for (int i = 1; i < PinLen; i++) {
						prec--;
						if (PIN2[i] != prec) {
							isSequence = false;
							break;
						}
					}
					if (isSequence && !again) {
						UIhelper::showPassiveMessage("Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido");
                                        	again = true;
					}
				}
                        }
                }
        } while(again);

        return ret;
#endif
}
#endif
