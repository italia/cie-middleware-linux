#pragma once

class safeDesktop {
	HDESK hSecureDesktop;
	HDESK hDeskCur;
public:
	safeDesktop(const char *name);
	operator HDESK();
	~safeDesktop();
};
