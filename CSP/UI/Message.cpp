// PinTDlg.cpp : implementazione di CPinTDlg

#include "../StdAfx.h"
#include "Message.h"
#ifndef WIN32
	#include "helper.h"
#endif

CMessage::CMessage(DWORD tipo, const char *title, const char *riga1, const char *riga2, const char *riga3, const char *riga4)
{
	this->title = title;
	this->riga1 = riga1;
	this->riga2 = riga2;
	this->riga3 = riga3;
	this->riga4 = riga4;
	this->tipo = tipo;
	if (riga1 != NULL && riga2 == NULL&& riga3 == NULL&& riga4 == NULL) {
		this->riga2 = riga1;
		this->riga1 = NULL;
	}
#ifdef WIN32
	txtFont = CreateFont(20, 0, 0, 0, 800, FALSE, FALSE, FALSE, ANSI_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS, 5, DEFAULT_PITCH, "Arial");
#else
	//std::cout << __FILE__ << ": " << title << std::endl << riga1 << std::endl << riga2 << std::endl << riga3 << std::endl << riga4 << std::endl;
#endif
}

CMessage::~CMessage()
{
#ifdef WIN32
	DeleteObject(txtFont);
#endif
}

LRESULT CMessage::OnBGnBrush(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled) {
#ifdef WIN32
	bHandled = TRUE;
	SetBkMode((HDC)wParam, TRANSPARENT);
	return (INT_PTR)::GetStockObject(NULL_PEN);
#else
	return 0;
#endif
}

LRESULT CMessage::OnHitTest(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled) {
#ifdef WIN32
	bHandled = TRUE;
	return (LRESULT)HTCAPTION;
#else
	return 0;
#endif
}

LRESULT CMessage::OnCtlColor(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled)  {
#ifdef WIN32
	bHandled = TRUE;
	SetBkMode((HDC)wParam, TRANSPARENT);
	return (INT_PTR)::GetStockObject(NULL_PEN);
#else
	return 0;
#endif
}

LRESULT CMessage::OnInitDialog(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
#ifdef WIN32
	CenterWindow();
	SetIcon(LoadIcon((HINSTANCE)moduleInfo.getModule(), MAKEINTRESOURCE(IDI_CIE)));

	BOOL r = okButton.SubclassWindow(GetDlgItem(IDOK));
	r = cancelButton.SubclassWindow(GetDlgItem(IDCANCEL));
	okButton.LoadStateBitmaps(IDB_OK, IDB_OK, IDB_OK2);
	cancelButton.LoadStateBitmaps(IDB_CANCEL, IDB_CANCEL, IDB_CANCEL2);
	backGround.LoadImageResource(IDB_BACKGROUND);
	
	tit = GetDlgItem(IDC_TITLE);

	if (tipo == MB_OK) {
		cancelButton.ShowWindow(SW_HIDE);
		WINDOWPLACEMENT bp;
		RECT wp;
		ZeroMem(bp);
		ZeroMem(wp);
		okButton.GetWindowPlacement(&bp);
		GetClientRect(&wp);
		okButton.SetWindowPos(NULL, (wp.right - (bp.rcNormalPosition.right - bp.rcNormalPosition.left)) / 2, bp.rcNormalPosition.top, 0, 0, SWP_NOSIZE);
	}
	if (tipo == MB_CANCEL) {
		okButton.ShowWindow(SW_HIDE);
		WINDOWPLACEMENT bp;
		RECT wp;
		ZeroMem(bp);
		ZeroMem(wp);
		cancelButton.GetWindowPlacement(&bp);
		GetClientRect(&wp);
		cancelButton.SetWindowPos(NULL, (wp.right - (bp.rcNormalPosition.right - bp.rcNormalPosition.left)) / 2, bp.rcNormalPosition.top, 0, 0, SWP_NOSIZE);
	}

	if (title != NULL) {
		tit.SetFont(txtFont, FALSE);
		tit.SetWindowTextA(title);
	}

	GetDlgItem(IDC_MSG1).SetFont(txtFont, FALSE);
	GetDlgItem(IDC_MSG2).SetFont(txtFont, FALSE);
	GetDlgItem(IDC_MSG3).SetFont(txtFont, FALSE);
	GetDlgItem(IDC_MSG4).SetFont(txtFont, FALSE);
	SetDlgItemText(IDC_MSG1, riga1);
	SetDlgItemText(IDC_MSG2, riga2);
	SetDlgItemText(IDC_MSG3, riga3);
	SetDlgItemText(IDC_MSG4, riga4);

	return 0;  // Lo stato attivo verrà impostato da me
#else
	return 0;
#endif
}

LRESULT CMessage::OnClickedOK(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
#ifdef WIN32
	EndDialog(wID);
	return 0;
#else
	return 0;
#endif
}

LRESULT CMessage::OnClickedCancel(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled)
{
#ifdef WIN32
	EndDialog(wID);
	return 0;
#else
	return 0;
#endif
}

LRESULT CMessage::OnPaint(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled)
{
#ifdef WIN32
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

#ifndef WIN32
INT_PTR CMessage::DoModal()
{
	INT_PTR ret = UIhelper::makeMessageDialog(riga1, riga2, riga3, riga4, title, tipo);
	return ret;
}
#endif
