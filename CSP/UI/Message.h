// CodiceBusta.h : Dichiarazione di CCodiceBusta

#pragma once

#include "AtlBitmapSkinButton.h"
#include "atlcontrols.h"
#ifdef WIN32
#include "../res/resource.h"       // simboli principali
#include <windows.h>       // simboli principali
#endif
#include "../Util/ModuleInfo.h"
#include "VCEdit.h"

#define MB_CANCEL 7

// CCodiceBusta
extern CModuleInfo moduleInfo;

class CMessage 
#ifdef WIN32
: public CDialogImpl<CMessage>
#endif
{
public:
#ifdef WIN32
	CStatic tit;
	CAtlBitmapButton okButton,cancelButton;
	CBitmap backGround;
	HFONT txtFont;
#endif
	const char *title;
	const char *riga1;
	const char *riga2;
	const char *riga3;
	const char *riga4;
	DWORD tipo;

	CMessage(DWORD tipo, const char *title, const char *riga1, const char *riga2 = NULL, const char *riga3 = NULL, const char *riga4 = NULL);

	~CMessage();
#ifdef WIN32
	enum { IDD = IDD_MESSAGE };

	BEGIN_MSG_MAP(CMessage)
	MESSAGE_HANDLER(WM_INITDIALOG, OnInitDialog)
	COMMAND_HANDLER(IDOK, BN_CLICKED, OnClickedOK)
	COMMAND_HANDLER(IDCANCEL, BN_CLICKED, OnClickedCancel)
	MESSAGE_HANDLER(WM_PAINT, OnPaint)
	MESSAGE_HANDLER(WM_CTLCOLORSTATIC, OnCtlColor)
	MESSAGE_HANDLER(WM_CTLCOLORDLG, OnBGnBrush)
	MESSAGE_HANDLER(WM_NCHITTEST, OnHitTest)
	REFLECT_NOTIFICATIONS()
	END_MSG_MAP()
#endif

// Prototipi di gestori:
//  LRESULT MessageHandler(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
//  LRESULT CommandHandler(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
//  LRESULT NotifyHandler(int idCtrl, LPNMHDR pnmh, BOOL& bHandled);

	LRESULT OnBGnBrush(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnHitTest(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnCtlColor(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnInitDialog(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnClickedOK(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);

	LRESULT OnClickedCancel(WORD wNotifyCode, WORD wID, HWND hWndCtl, BOOL& bHandled);
	
	LRESULT OnPaint(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
#ifndef WIN32
	INT_PTR DoModal();
#endif
	BOOL EndDialog(HWND hDlg, INT_PTR nResult=0){return 1;}
};


