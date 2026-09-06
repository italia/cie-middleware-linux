/*
 *  PdfVerifier.h
 *  SignPoDoFo
 *
 *  Created by svp on 26/05/12.
 *  Copyright 2012 __MyCompanyName__. All rights reserved.
 *
 */

#ifndef _PDFVERIFIER_H_
#define _PDFVERIFIER_H_


#include "podofo/podofo.h"

#if PODOFO_VERSION_MAJOR > 0 || PODOFO_VERSION_MINOR >= 10
#define CIE_PODOFO_MODERN 1
#else
#define CIE_PODOFO_MODERN 0
#endif
#include "ASN1/UUCByteArray.h"
#include "disigonsdk.h"

using namespace PoDoFo;
using namespace std;

inline void ObjectToString(const PdfObject* pObj, std::string& out)
{
	out.clear();
	if (pObj == 0)
		return;

	pObj->ToString(out);

	while (!out.empty())
	{
		const char c = out[out.size() - 1];
		if (c != '\n' && c != '\r' && c != ' ' && c != '\t')
			break;
		out.erase(out.size() - 1);
	}
}

typedef struct _SignatureAppearanceInfo
{
	int left;
	int bottom;
	int width;
	int heigth;
} SignatureAppearanceInfo;

class PDFVerifier 
{
public:
	PDFVerifier();
	
	virtual ~PDFVerifier();
	
	int Load(const char* pdf, int len);
    int Load(const char* szFilePath);
	
	int GetNumberOfSignatures();
	
	int VerifySignature(int index, const char* szDate, char* signatureType, REVOCATION_INFO* pRevocationInfo);
	
	
	int GetSignature(int index, UUCByteArray& signedDocument, SignatureAppearanceInfo& appearanceInfo);
	
	static int GetNumberOfSignatures(PdfMemDocument* pPdfDocument);
    static int GetNumberOfSignatures(const char* szFilePath);
	
private:
	UUCByteArray m_data;
	static bool IsSignatureField(const PdfMemDocument* pDoc, const PdfObject *const pObj);
	
	int VerifySignature(const PdfMemDocument* pDoc, const PdfObject *const pObj, const char* szDate, char* signatureType, REVOCATION_INFO* pRevocationInfo);

	int GetSignature(const PdfMemDocument* pDoc, const PdfObject *const pObj, UUCByteArray& signedDocument, SignatureAppearanceInfo& appearanceInfo);

	PdfMemDocument* m_pPdfDocument;
	
	int m_actualLen;
	
	char* m_szDocBuffer;
	
};

#endif //_PDFVERIFIER_H




