#include <string.h>
#include <stdio.h>
#include <iostream>
#include "../Util/defines.h"

using namespace std;

namespace UIhelper {

enum BackendType {BACKEND_ZENITY=0, BACKEND_KDIALOG=1, BACKEND_NONE=-1};

static int execute(const char *cmdline, char *dataIn, int len, HWND *wnd=nullptr)
{
	FILE *f = popen(cmdline, wnd?"w":"r");
	if(wnd)
		*((FILE**)wnd) = f;
	else
	{
		fgets(dataIn, len, f);
		int ex_status = pclose(f);
		return  WEXITSTATUS(ex_status);
	}

	return 0;
}

static BackendType getBackend()
{
	static BackendType backend = BACKEND_NONE;

	if(backend != BACKEND_NONE)
		return backend;

	char dataIn[1024];
	int ret = execute("which zenity", dataIn, sizeof(dataIn));
	if(ret == 0)
	{
		backend = BACKEND_ZENITY;
		return backend;
	}

	ret = execute("which kdialog", dataIn, sizeof(dataIn));
	if(ret == 0)
	{
		backend = BACKEND_KDIALOG;
		return backend;
	}

	return BACKEND_NONE;
}

static void stripCrlf(char *dataIn)
{
	int pos;

	do {
		pos = strlen(dataIn);
		if(pos > 0 && (dataIn[pos-1] == '\r' || dataIn[pos-1] == '\n'))
			dataIn[pos-1] = '\0';
		else break;
	} while (pos > 0);
}

INT_PTR makeDualPinDialog(int PinLen, const char *message, const char *message2, const char *message3, const char *title, char *destPin1, char *destPin2)
{
	BackendType backend = getBackend();
	if(backend < 0)
		return -1;
	string msg(message?message:"");
	const char *crlf = backend==BACKEND_ZENITY ? "\n" : "<br>";
	if(message2 && strlen(message2) > 0) {
		msg += crlf;
		msg += message2;
	}
	if(message3 && strlen(message3) > 0) {
		msg += crlf;
	       	msg += message3;
	}
	const char *cmdLine[] = {
		"zenity --forms --title=\"%s\" --text=\"%s\" --add-password=\"PIN\" --add-password=\"Conferma PIN\" --width=350 --height=150",
		"kdialog --title \"%s\" --password \"%s\""
	};
	char dataIn[1024];
	char cmd[300];
	char *dst[] = {destPin1, destPin2};
	sprintf(cmd, cmdLine[backend], title, msg.c_str());
	int ret = execute(cmd, dataIn, sizeof(dataIn));
	switch(ret)
	{
		case 1://cancel
			destPin1[0] = '\0';
			destPin2[0] = '\0';
			return IDCANCEL;
		case 0://ok
			destPin1[PinLen] = '\0';
			destPin2[PinLen] = '\0';

			const char s[2] = "|";
			char *token = strtok(dataIn, s);
			int count = 0;
			while( token != NULL && count < 2) {
				strncpy(dst[count++], token, PinLen);
				token = strtok(NULL, s);
			}
			destPin1[PinLen] = '\0';
			destPin2[PinLen] = '\0';

			stripCrlf(destPin1);
			stripCrlf(destPin2);

			return IDOK;
	}

	return -1;
}

INT_PTR makePinDialog(int PinLen, const char *message, const char *message2, const char *message3, const char *title, char *destPin)
{
	BackendType backend = getBackend();
	if(backend < 0)
		return -1;
	string msg(message?message:"");
	const char *crlf = backend==BACKEND_ZENITY ? "\n" : "<br>";
	if(message2 && strlen(message2) > 0) {
		msg += crlf;
		msg += message2;
	}
	if(message3 && strlen(message3) > 0) {
		msg += crlf;
	       	msg += message3;
	}
	const char *cmdLine[] = {
		"zenity --entry --title=\"%s\" --text=\"%s\" --hide-text --width=350 --height=150",
		"kdialog --title \"%s\" --password \"%s\""
	};
	char dataIn[1024];
	char cmd[300];
	sprintf(cmd, cmdLine[backend], title, msg.c_str());
	int ret = execute(cmd, dataIn, sizeof(dataIn));
	switch(ret)
	{
		case 1://cancel
			destPin[0] = '\0';
			return IDCANCEL;
		case 0://ok
			strncpy(destPin, dataIn, PinLen);
			destPin[PinLen] = '\0';
			stripCrlf(destPin);

			return IDOK;
	}

	return -1;
}


INT_PTR makeMessageDialog(const char *message, const char *message1, const char *message2, const char *message3, const char *title, int type)
{
	BackendType backend = getBackend();
	if(backend < 0)
		return -1;
	string msg(message?message:"");
	const char *crlf = backend==BACKEND_ZENITY ? "\n" : "<br>";
	if(message1 && strlen(message1) > 0) {
		msg += crlf;
		msg += message1;
	}
	if(message2 && strlen(message2) > 0) {
		msg += crlf;
		msg += message2;
	}
	if(message3 && strlen(message3) > 0) {
		msg += crlf;
	       	msg += message3;
	}
	const char *cmdLine[][2] = {
		{"zenity --info --ok-label=\"Ok\" --title=\"%s\" --text=\"%s\" --width=350 --height=150",
		"kdialog --title \"%s\"  --msgbox \"%s\""},
		{"zenity --question --ok-label=\"Ok\" --cancel-label=\"Annulla\" --title=\"%s\" --text=\"%s\" --width=350 --height=150",
		"kdialog --title \"%s\" --warningcontinuecancel \"%s\""}
	};
	char dataIn[1024];
	char cmd[300];
	sprintf(cmd, cmdLine[type==MB_OKCANCEL?1:0][backend], title, msg.c_str());
	int ret = execute(cmd, dataIn, sizeof(dataIn));
	switch(ret)
	{
		case 2:
		case 1://cancel
			return IDCANCEL;
		case 0://ok
			return IDOK;
	}

	return -1;
}


void showPassiveMessage(const char *msg, const char *title)
{
	BackendType backend = getBackend();
	if(backend < 0)
		return;

	char dataIn[1024];
	char cmd[300];

	const char *cmdLine[] = {
		"zenity --error --title=\"%s\" --text=\"%s\" --width=350 --height=150",
		"kdialog --title \"%s\" --passivepopup \"%s\" 5"
	};
	sprintf(cmd, cmdLine[backend], title, msg);
	int ret = execute(cmd, dataIn, sizeof(dataIn));
}

void makeProgressDialog(int steps, HWND *wnd)
{
	static std::string handleKdialog;	//these statics mean that you annot open more than one progress box at once
	static FILE *handleZenity = nullptr;

	BackendType backend = getBackend();
	if(backend < 0)
		return;

	switch(backend)
	{
		case BACKEND_KDIALOG:
			*wnd = &handleKdialog;
			break;

		case BACKEND_ZENITY:
			*wnd = handleZenity;
			break;
	}
			
	char dataIn[1024];
	char cmd[300];

	const char *cmdLine[] = {
		"zenity --progress --text=\"\" --title=\"\" --percentage=0 --auto-close --no-cancel --width=350 --height=70",
		"kdialog --progressbar \"\" %d"
	};
	sprintf(cmd, cmdLine[backend], steps);
	int ret = execute(cmd, dataIn, sizeof(dataIn), (backend == BACKEND_ZENITY ? wnd : nullptr));
	//TODO: check if ret == 0?

	switch(backend)
	{
		case BACKEND_KDIALOG:
		{
			stripCrlf(dataIn);
			string handle = string(dataIn);

			sprintf(cmd, "qdbus %s showCancelButton false", dataIn);
			ret = execute(cmd, dataIn, sizeof(dataIn));

			**(std::string**)wnd = handle;
		}
			break;

		case BACKEND_ZENITY:
			//'wnd' set inside execute()
			break;
	}
}

#if 0
////////////////////////////////////
INT_PTR DoModal(char *PIN);
int main()
{
	char destPin[10];
	char destPin2[10];
	destPin[0] = '\0';
	destPin2[0] = '\0';
	//INT_PTR ret = makePinDialog(8, "msg1", "msg2", "msg3", "title", destPin);
	//INT_PTR ret = makeMessageDialog("msg0", "msg1", "msg2", "msg3", "title", MB_OK);
	//INT_PTR ret = makeProgressDialog("msg0", "msg1", "msg2", "msg3", "title", MB_OK);
	//INT_PTR ret = makeDualPinDialog(8, "msg0", "msg1", "msg2", "title", destPin, destPin2);
	INT_PTR ret = DoModal(destPin);
	if(ret == IDOK)
	{
		cout << "PIN entered: " << destPin << endl;
		cout << "2nd PIN:     " << destPin2 << endl;
	}
	cout << "Retcode: " << ret << endl;
	return 0;
}
#endif

}

LRESULT WINAPI SendMessage(HWND hWnd, UINT Msg, WPARAM wParam, LPARAM lParam)
{
	UIhelper::BackendType backend = UIhelper::getBackend();
	if(backend < 0)
		return -1;

	char dataIn[1024];
	char cmd[300];

	switch(backend)
	{
		case UIhelper::BACKEND_KDIALOG:
		{
			if((std::string*)hWnd && ((std::string*)hWnd)->length() > 0)
			{
				const char *hnd = ((std::string*)hWnd)->c_str();
				if(wParam == 100+7)
				{
					sprintf(cmd, "qdbus %s close", hnd);
					int ret = UIhelper::execute(cmd, dataIn, sizeof(dataIn));

					return ret;
				}

				sprintf(cmd, "qdbus %s setLabelText \"%s\"", hnd, (const char*)lParam);
				int ret = UIhelper::execute(cmd, dataIn, sizeof(dataIn));

				sprintf(cmd, "qdbus %s Set \"\" value %d", hnd, wParam-100);
				ret = UIhelper::execute(cmd, dataIn, sizeof(dataIn));
			}
		}
			break;

		case UIhelper::BACKEND_ZENITY:			
		{
			FILE *f = (FILE*)hWnd;
			if(f)
			{
				fprintf(f, "%d\n# %s\n", (wParam-100)*100/7, (const char*)lParam);
				fflush(f);
				if(wParam == 100+7)
					pclose(f);
			}
		}
			break;
	}

	return 0;
}

