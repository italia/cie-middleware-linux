#ifdef WIN32
#include "stdafx.h"
#include "sddl.h"
#include "Aclapi.h"
#include <VersionHelpers.h>
#include <Shlwapi.h>
#include <Shlobj.h>
#else
#include <sys/stat.h>
#include <stdlib.h>
#endif
#include "CacheLib.h"
#include <stdio.h>
#include <vector>
#include <fstream>

#include "../CSP/Util/util.h"

#define MAX_PATH	256

/// Questa implementazione della cache del PIN e del certificato è fornita solo a scopo dimostrativo. Questa versione
/// NON protegge a sufficienza il PIN dell'utente, che potrebbe essere ricavato da un'applicazione malevola. Si raccomanda di
/// utilizzare, in contesti di produzione, un'implementazione che fornisca un elevato livello di sicurezza

std::string commonData;

bool CreateDirectory(const char *chDir, void *flags)
{
	const int dir_err = mkdir(chDir, S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
	return (-1 != dir_err);
}

bool PathFileExists(const char *path)
{
	struct stat buffer;
	return (stat(path, &buffer) == 0);
}

std::string GetCardDir() {

	if (commonData[0] == 0) {
		char szPath[MAX_PATH];
#ifdef WIN32
		ExpandEnvironmentStrings("%PROGRAMDATA%\\CIEPKI", szPath, MAX_PATH);
#else
		const char *temp = getenv("CIEDIR");
  		if (temp != NULL) {
			strncpy(szPath, temp, MAX_PATH);
		}
		else strcpy(szPath, "");
#endif
		commonData = szPath;
	}
	return commonData;
}

void GetCardPath(const char *PAN, char szPath[MAX_PATH]) {
	auto Path=GetCardDir();

	if (Path[Path.length()] != PATH_SEPARATOR)
		Path += PATH_SEPARATOR;

	Path += std::string(PAN);
	Path += ".cache";
	strncpy(szPath, Path.c_str(), MAX_PATH);
}

bool CacheExists(const char *PAN) {
	char szPath[MAX_PATH];
	GetCardPath(PAN, szPath);
	return (PathFileExists(szPath)!=false);
}

void CacheGetCertificate(const char *PAN, std::vector<uint8_t>&certificate)
{
	if (PAN == nullptr)
		throw logged_error("Il PAN è necessario");

	char szPath[MAX_PATH];
	GetCardPath(PAN, szPath);

	if (PathFileExists(szPath)) {

		ByteDynArray data, Cert;
		data.load(szPath);
		uint8_t *ptr = data.data();
		uint32_t len = *(uint32_t*)ptr; ptr += sizeof(uint32_t);
		// salto il PIN
		ptr += len;
		len = *(uint32_t*)ptr; ptr += sizeof(uint32_t);
		Cert.resize(len); Cert.copy(ByteArray(ptr, len));

		certificate.resize(Cert.size());
		ByteArray(certificate.data(), certificate.size()).copy(Cert);
	}
	else
	{
		throw logged_error("CIE non abilitata");
	}
}

void CacheGetPIN(const char *PAN, std::vector<uint8_t>&PIN) {
	if (PAN == nullptr)
		throw logged_error("Il PAN è necessario");

	char szPath[MAX_PATH];
	GetCardPath(PAN, szPath);

	if (PathFileExists(szPath)) {
		ByteDynArray data, ClearPIN;
		data.load(szPath);
		uint8_t *ptr = data.data();
		uint32_t len = *(uint32_t*)ptr; ptr += sizeof(uint32_t);
		ClearPIN.resize(len); ClearPIN.copy(ByteArray(ptr, len));

		PIN.resize(ClearPIN.size());
		ByteArray(PIN.data(), PIN.size()).copy(ClearPIN);

	}
	else
		throw logged_error("CIE non abilitata");
		
}



void CacheSetData(const char *PAN, uint8_t *certificate, int certificateSize, uint8_t *FirstPIN, int FirstPINSize) {
	if (PAN == nullptr)
		throw logged_error("Il PAN è necessario");

	auto szDir=GetCardDir();
	char chDir[MAX_PATH];
	strcpy(chDir, szDir.c_str());

	if (!PathFileExists(chDir)) {
		
		//creo la directory dando l'accesso a Edge (utente Packege).
		//Edge gira in low integrity quindi non potrà scrivere (enrollare) ma solo leggere il certificato
		bool done = false;
		CreateDirectory(chDir, nullptr);
#ifdef WIN32
		if (IsWindows8OrGreater()) {
			PACL pOldDACL = nullptr, pNewDACL = nullptr;
			PSECURITY_DESCRIPTOR pSD = nullptr;
			EXPLICIT_ACCESS ea;
			SECURITY_INFORMATION si = DACL_SECURITY_INFORMATION;

			DWORD dwRes = GetNamedSecurityInfo(chDir, SE_FILE_OBJECT, DACL_SECURITY_INFORMATION, NULL, NULL, &pOldDACL, NULL, &pSD);
			if (dwRes != ERROR_SUCCESS)
				throw logged_error("Impossibile attivare la CIE nel processo corrente");

			PSID TheSID = nullptr;
			DWORD SidSize = SECURITY_MAX_SID_SIZE;
			if (!(TheSID = LocalAlloc(LMEM_FIXED, SidSize))) {
				if (pSD != NULL)
					LocalFree((HLOCAL)pSD);
				throw logged_error("Impossibile attivare la CIE nel processo corrente");
			}

			if (!CreateWellKnownSid(WinBuiltinAnyPackageSid, NULL, TheSID, &SidSize)) {
				if (TheSID != NULL)
					LocalFree((HLOCAL)TheSID);
				if (pSD != NULL)
					LocalFree((HLOCAL)pSD);
				throw logged_error("Impossibile attivare la CIE nel processo corrente");
			}

			ZeroMemory(&ea, sizeof(EXPLICIT_ACCESS));
			ea.grfAccessPermissions = GENERIC_READ;
			ea.grfAccessMode = SET_ACCESS;
			ea.grfInheritance = SUB_CONTAINERS_AND_OBJECTS_INHERIT;
			ea.Trustee.TrusteeForm = TRUSTEE_IS_SID;
			ea.Trustee.TrusteeType = TRUSTEE_IS_WELL_KNOWN_GROUP;
			ea.Trustee.ptstrName = (LPSTR)TheSID;

			if (SetEntriesInAcl(1, &ea, pOldDACL, &pNewDACL) != ERROR_SUCCESS)
			{
				if (TheSID != NULL)
					LocalFree((HLOCAL)TheSID);
			if (pSD != NULL)
				LocalFree((HLOCAL)pSD);
			if (pNewDACL != NULL)
				LocalFree((HLOCAL)pNewDACL);
				throw logged_error("Impossibile attivare la CIE nel processo corrente");
			}

			if (SetNamedSecurityInfo(chDir, SE_FILE_OBJECT, si, NULL, NULL, pNewDACL, NULL) != ERROR_SUCCESS)
			{
				if (pNewDACL != NULL)
					LocalFree((HLOCAL)pNewDACL);
			if (TheSID != NULL)
				LocalFree((HLOCAL)TheSID);
				if (pSD != NULL)
					LocalFree((HLOCAL)pSD);
				throw logged_error("Impossibile attivare la CIE nel processo corrente");
			}

		}
#endif
	}
	char szPath[MAX_PATH];
	GetCardPath(PAN, szPath);

	ByteArray baCertificate(certificate, certificateSize);
	ByteArray baFirstPIN(FirstPIN, FirstPINSize);

	std::ofstream file(szPath, std::ofstream::out | std::ofstream::binary);

	uint32_t len = (uint32_t)baFirstPIN.size();
	file.write((char*)&len, sizeof(len));
	file.write((char*)baFirstPIN.data(), len);

	len = (uint32_t)baCertificate.size();
	file.write((char*)&len, sizeof(len));
	file.write((char*)baCertificate.data(), len);
}
