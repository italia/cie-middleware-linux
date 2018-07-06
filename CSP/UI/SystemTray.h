#ifndef _WIN32_IE
#define _WIN32_IE 0x0501
#endif

#ifdef WIN32
#include <ShellAPI.h>
#endif

#pragma once

#include <time.h>
#include <vector>
using namespace std;
typedef vector<HICON> ICONVECTOR;

class CSystemTray
{
// Construction/destruction
public:
    CSystemTray(){};
    CSystemTray(HINSTANCE hInst, HWND hParent, UINT uCallbackMessage, 
              LPCTSTR szTip, HICON icon, UINT uID, 
              BOOL bhidden = false,
              LPCTSTR szBalloonTip = NULL, LPCTSTR szBalloonTitle = NULL, 
      DWORD dwBalloonIcon = NIIF_NONE, UINT uBalloonTimeout = 10){std::cout << __FILE__ << ": " << szBalloonTitle << std::endl << szBalloonTip << std::endl;}
    virtual ~CSystemTray() {};

// Operations
public:
    BOOL Enabled() { return m_bEnabled; }
    BOOL Visible() { return !m_bHidden; }

    // Create the tray icon
    BOOL Create(HINSTANCE hInst, HWND hParent, UINT uCallbackMessage, LPCTSTR szTip,
		   HICON icon, UINT uID, BOOL bHidden = false,
           LPCTSTR szBalloonTip = NULL, LPCTSTR szBalloonTitle = NULL, 
           DWORD dwBalloonIcon = NIIF_NONE, UINT uBalloonTimeout = 10){return 0;}

    // Change or retrieve the Tooltip text
    BOOL   SetTooltipText(LPCTSTR pszTooltipText){return 0;}
    BOOL   SetTooltipText(UINT nID){return 0;}
    LPTSTR GetTooltipText() const {return "--- tooltip ---";}

    // Change or retrieve the icon displayed
    BOOL  SetIcon(HICON hIcon){return 0;}
    BOOL  SetIcon(LPCTSTR lpszIconName){return 0;}
    BOOL  SetIcon(UINT nIDResource){return 0;}
    BOOL  SetStandardIcon(LPCTSTR lpIconName){return 0;}
    BOOL  SetStandardIcon(UINT nIDResource){return 0;}
    HICON GetIcon() const{return nullptr;}

    void  SetFocus(){}
    BOOL  HideIcon(){return 0;}
    BOOL  ShowIcon(){return 0;}
    BOOL  AddIcon(){return 0;}
    BOOL  RemoveIcon(){return 0;}

    BOOL ShowBalloon(LPCTSTR szText, LPCTSTR szTitle = NULL,
                     DWORD dwIcon = NIIF_NONE, UINT uTimeout = 10){std::cout << __FILE__ << ": " << szTitle << std::endl << szText << std::endl;return 0;}

    // Change or retrieve the window to send icon notification messages to
    BOOL  SetNotificationWnd(HWND hNotifyWnd){return 0;}
    HWND  GetNotificationWnd() const{return nullptr;}

    // Change or retrieve the window to send menu commands to
    BOOL  SetTargetWnd(HWND hTargetWnd){return 0;}
    HWND  GetTargetWnd() const{return nullptr;}

    // Change or retrieve  notification messages sent to the window
    BOOL  SetCallbackMessage(UINT uCallbackMessage){return 0;}
    UINT  GetCallbackMessage() const{return 0;}

    HWND  GetSafeHwnd() const  { return (this)? m_hWnd : NULL; }

	// Static functions
public:
    static void MinimiseToTray(HWND hWnd){}
    static void MaximiseFromTray(HWND hWnd){}

public:
    // Default handler for tray notification message
	void(*TrayNotification)(CSystemTray* tray ,WPARAM uID, LPARAM lEvent);
	void(*TrayBaloonTimeout)(CSystemTray* tray);
	virtual LRESULT OnTrayNotification(WPARAM uID, LPARAM lEvent){return 0;}

// Overrides
    // ClassWizard generated virtual function overrides
    //{{AFX_VIRTUAL(CSystemTray)
	//}}AFX_VIRTUAL

// Static callback functions and data
public:
    static LRESULT PASCAL WindowProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam){return 0;}
    static CSystemTray* m_pThis;

// Implementation
protected:
    void Initialise(){}
    void InstallIconPending(){}
    ATOM RegisterClass(HINSTANCE hInstance){}

// Implementation
protected:
    NOTIFYICONDATA  m_tnd;
    HINSTANCE       m_hInstance;
    HWND            m_hWnd;
    HWND            m_hTargetWnd;       // Window that menu commands are sent

    BOOL            m_bEnabled;         // does O/S support tray icon?
    BOOL            m_bHidden;          // Has the icon been hidden?
    BOOL            m_bRemoved;         // Has the icon been removed?
    BOOL            m_bShowIconPending; // Show the icon once tha taskbar has been created

    ICONVECTOR      m_IconList; 
    int				m_nCurrentIcon;
    HICON			m_hSavedIcon;
    UINT			m_uCreationFlags;

// Static data
protected:
    static BOOL RemoveTaskbarIcon(HWND hWnd){return 0;}

    static UINT  m_nMaxTooltipLength;
    static const UINT m_nTaskbarCreatedMsg;
    static HWND  m_hWndInvisible;

    static BOOL GetW2K(){}
    static void GetTrayWndRect(LPRECT lprect){}

// message map functions
public:
    LRESULT OnTaskbarCreated(WPARAM wParam, LPARAM lParam){return 0;}
    LRESULT OnSettingChange(UINT uFlags, LPCTSTR lpszSection){return 0;}
};


