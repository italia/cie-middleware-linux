#pragma once

#include "../Util/defines.h"

namespace UIhelper {

	INT_PTR makeDualPinDialog(int PinLen, const char *message, const char *message2, const char *message3, const char *title, char *destPin1, char *destPin2);
	INT_PTR makePinDialog(int PinLen, const char *message, const char *message2, const char *message3, const char *title, char *destPin);
	INT_PTR makeMessageDialog(const char *message, const char *message1, const char *message2, const char *message3, const char *title, int type);
	void showPassiveMessage(const char *msg, const char *title);

}
