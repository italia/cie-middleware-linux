/*
 *  PdfSignatureGenerator.h
 *  SignPoDoFo
 *
 *  Created by svp on 26/05/12.
 *  Copyright 2012 __MyCompanyName__. All rights reserved.
 *
 */

#ifndef _PDFSIGNATUREGENERATOR_H_
#define _PDFSIGNATUREGENERATOR_H_

#include "podofo/podofo.h"
#include "ASN1/UUCByteArray.h"

#include <functional>

// PoDoFo 0.10 ha sostituito PdfSignOutputDevice con il modello a callback di PdfSigner.
// La 1.x mantiene lo stesso modello.
#if PODOFO_VERSION_MAJOR > 0 || PODOFO_VERSION_MINOR >= 10
#define CIE_PODOFO_MODERN 1
#else
#define CIE_PODOFO_MODERN 0
#endif

#if !CIE_PODOFO_MODERN
#include "podofo/doc/PdfSignOutputDevice.h"
#include "podofo/doc/PdfSignatureField.h"
#endif

using namespace PoDoFo;
using namespace std;

// Firma i byte che le vengono passati e restituisce la firma CMS.
typedef std::function<long(const unsigned char* data, unsigned long len,
                           UUCByteArray& signature)> CieSignCallback;

class PdfSignatureGenerator
{
public:
	PdfSignatureGenerator();
	
	virtual ~PdfSignatureGenerator();
	
	int Load(const char* pdf, int len);
	
	void InitSignature(int pageIndex, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter);
	
	void InitSignature(int pageIndex, float left, float bottom, float width, float height, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter);
	
	void InitSignature(int pageIndex, float left, float bottom, float width, float height, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter, const char* szImagePath, const char* szDescription, const char* szGraphometricData, const char* szVersion);
	
	long SignDocument(const CieSignCallback& signCallback, UUCByteArray& signedPdf);

#if !CIE_PODOFO_MODERN
	void GetBufferForSignature(UUCByteArray& toSign);
	
	void SetSignature(const char* signature, int len);
	
	void GetSignedPdf(UUCByteArray& signature);
#endif
	
	void AddFont(const char* szFontName, const char* szFontPath);
	
	const double getWidth(int pageIndex);
	
	const double getHeight(int pageIndex);
	
private:
	PdfMemDocument* m_pPdfDocument;

#if CIE_PODOFO_MODERN
	// Proprieta' del documento: da non cancellare.
	PdfSignature* m_pSignature;

	// SignDocument() fa aggiornamento incrementale.
	std::vector<char> m_originalPdf;

	// /SubFilter richiesto dal chiamante, girato al firmatario.
	std::string m_subFilter;
#else
	PdfSignatureField* m_pSignatureField;
	
	PdfSignOutputDevice* m_pSignOutputDevice;
	
	PdfOutputDevice* m_pFinalOutDevice;
	
	char* m_pMainDocbuffer;
	
	char* m_pSignDocbuffer;
#endif
	
	const double lastSignatureY(int left, int bottom);
	
	int m_actualLen;
	
	static bool IsSignatureField(const PdfMemDocument* pDoc, const PdfObject *const pObj);
	
};

#endif // _PDFSIGNATUREGENERATOR_H_
