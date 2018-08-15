// CodiceBusta.h : Dichiarazione di CCodiceBusta

#pragma once

#ifdef WIN32
#include "../res/resource.h"       // simboli principali
#include <windows.h>       // simboli principali
#endif
#include "../UI/AtlBitmapSkinButton.h"
#include "../UI/atlcontrols.h"
#include "../Util/ModuleInfo.h"
#include "../UI/VCEdit.h"


// CCodiceBusta
extern CModuleInfo moduleInfo;

class CVerifica
#ifdef WIN32
: public CDialogImpl<CVerifica>
#endif
{
public:
#ifdef WIN32
	CAtlBitmapButton okButton,cancelButton;
	CStatic tit;
	CBitmap backGround;
	HFONT txtFont;
	CProgressBarCtrl progress;
#endif
	HWND *wnd;
	CVerifica(HWND *wnd);

	~CVerifica();

#ifdef WIN32
	enum { IDD = IDD_VERIFY };

	BEGIN_MSG_MAP(CVerifica)
	MESSAGE_HANDLER(WM_COMMAND, OnCommand)
	MESSAGE_HANDLER(WM_INITDIALOG, OnInitDialog)
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
	LRESULT OnCommand(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);


	LRESULT OnBGnBrush(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnHitTest(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnCtlColor(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
	LRESULT OnInitDialog(UINT uMsg, WPARAM wParam, LPARAM lParam, BOOL& bHandled);

	LRESULT OnPaint(UINT /*uMsg*/, WPARAM wParam, LPARAM lParam, BOOL& bHandled);
#ifndef WIN32
	INT_PTR DoModal();
#endif
};


