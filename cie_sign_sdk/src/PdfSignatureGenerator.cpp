/*
 *  PdfSignatureGenerator.cpp
 *  SignPoDoFo
 *
 *  Created by svp on 26/05/12.
 *  Copyright 2012 __MyCompanyName__. All rights reserved.
 *
 */

#include "PdfSignatureGenerator.h"
#include "PdfVerifier.h"
#include "UUCLogger.h"

#define SINGNATURE_SIZE 10000

#if !CIE_PODOFO_MODERN

#ifdef CreateFont
#undef CreateFont
#endif

#ifdef GetObject
#undef GetObject
#endif

int GetNumberOfSignatures(PdfMemDocument* pPdfDocument);

// Etichette del riquadro di firma visibile.
struct SignatureLabels
{
	std::string reason;
	std::string name;
	std::string location;
};

static void SetSignatureAppearance(PdfSignatureField* pField,
                                   PdfMemDocument* pDoc,
                                   const char* szImagePath,
                                   const char* szDescription,
                                   const SignatureLabels& labels);

static void SetSignatureGraphometricData(PdfSignatureField* pField,
                                         const PdfString& rsName,
                                         const PdfString& rsData,
                                         const PdfString& rsVersion);

// Prima era SetSignatureReason(label, text) a inserire le stringhe nel dizionario della firma
static void SetSignatureDictString(PdfSignatureField* pField,
                                   const char* szKey,
                                   const PdfString& rsValue)
{
	pField->EnsureSignatureObject();
	PdfDictionary& dict = pField->GetSignatureObject()->GetDictionary();

	PdfName key(szKey);
	if (dict.HasKey(key))
		dict.RemoveKey(key);

	dict.AddKey(key, PdfObject(rsValue));
}

USE_LOG;

PdfSignatureGenerator::PdfSignatureGenerator()
: m_pPdfDocument(NULL), m_pSignatureField(NULL), m_pSignOutputDevice(NULL), m_pFinalOutDevice(NULL),
m_pMainDocbuffer(NULL), m_pSignDocbuffer(NULL)
{
	PoDoFo::PdfError::EnableLogging(false);
}

PdfSignatureGenerator::~PdfSignatureGenerator()
{
	if(m_pPdfDocument)
		delete m_pPdfDocument;
	
	if(m_pSignatureField)
		delete m_pSignatureField;
	
	if(m_pSignOutputDevice)
		delete m_pSignOutputDevice;
	
	if(m_pFinalOutDevice)
		delete m_pFinalOutDevice;
	
	if(m_pMainDocbuffer)
		delete m_pMainDocbuffer;
	
	if(m_pSignDocbuffer)
		delete m_pSignDocbuffer;
	
}

int PdfSignatureGenerator::Load(const char* pdf, int len)
{
	if(m_pPdfDocument)
		delete m_pPdfDocument;
	
	try
	{
        printf("PDF");
        //printf("%s", (char *)pdf);
        printf("LENGTH");
        printf("%i", len);
        printf("STOP");

		m_pPdfDocument = new PdfMemDocument();
		m_pPdfDocument->LoadFromBuffer(pdf, len);
		printf("OK m_pPdfDocument");
		int nSigns = PDFVerifier::GetNumberOfSignatures(m_pPdfDocument);
		printf("OK nSigns: %d", nSigns);

		if(nSigns > 0)
		{
			delete m_pPdfDocument;
			m_pPdfDocument = new PdfMemDocument();
			m_pPdfDocument->LoadFromBuffer(pdf, len, true);
		}
		m_actualLen = len;
		
		return nSigns;
	}
    catch(::PoDoFo::PdfError& err)
    {
        return -2;
    }
	catch (...)
	{
		return -1;
	}
}

void PdfSignatureGenerator::AddFont(const char* szFontName, const char* szFontPath)
{
	//printf(szFontName);
	//printf(szFontPath);
	
	
	// 0.9.8 ha inserito bSymbolCharset fra bItalic e pEncoding.
	PdfFont* font = m_pPdfDocument->CreateFont(szFontName, false, false, false, PdfEncodingFactory::GlobalWinAnsiEncodingInstance(), PdfFontCache::eFontCreationFlags_AutoSelectBase14, true, szFontPath);
	PdfFont* font1 = m_pPdfDocument->CreateFont(szFontName, true, false, false, PdfEncodingFactory::GlobalWinAnsiEncodingInstance(), PdfFontCache::eFontCreationFlags_AutoSelectBase14, true, szFontPath);
}

void PdfSignatureGenerator::InitSignature(int pageIndex, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter)
{
	LOG_DBG((0, "quella con tutti 0\n", ""));
	InitSignature(pageIndex, 0, 0, 0, 0, szReason, szReasonLabel, szName, szNameLabel, szLocation, szLocationLabel, szFieldName, szSubFilter);
}

void PdfSignatureGenerator::InitSignature(int pageIndex, float left, float bottom, float width, float height, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter)
{
	LOG_DBG((0,"quella senza tutti 0\n", ""));
	InitSignature(pageIndex, left, bottom, width,  height, szReason, szReasonLabel, szName, szNameLabel, szLocation, szLocationLabel, szFieldName, szSubFilter, NULL, NULL, NULL, NULL);
}

void PdfSignatureGenerator::InitSignature(int pageIndex, float left, float bottom, float width, float height, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter, const char* szImagePath, const char* szDescription, const char* szGraphometricData, const char* szVersion)
{
	LOG_DBG((0, "--> InitSignature", "%d, %f, %f, %f, %f, %s, %s, %s, %s, %s, %s, %s, %s", pageIndex, left, bottom, width, height, szReason, szName, szLocation, szFieldName, szSubFilter, szImagePath, szGraphometricData, szVersion));
    //LOG_DBG((0, "--> InitSignature", ""));
    
             
	if(m_pSignatureField)
		delete m_pSignatureField;

	PdfPage* pPage = m_pPdfDocument->GetPage(pageIndex);
    PdfRect cropBox = pPage->GetCropBox();
    
    float left0 = left * cropBox.GetWidth();
    float bottom0 = cropBox.GetHeight() - (bottom * cropBox.GetHeight());
    
    float width0 = width * cropBox.GetWidth();
    float height0 = height * cropBox.GetHeight();
    
    printf("pdf rect: %f, %f, %f, %f\n", left0, bottom0, width0, height0);
    
	PdfRect rect(left0, bottom0, width0, height0);
	
	LOG_DBG((0, "InitSignature", "PdfSignatureField"));

        // m_pSignatureField = new PdfSignatureField(pPage, rect, m_pPdfDocument, PdfString(szFieldName), szSubFilter);
	m_pSignatureField = new PdfSignatureField(pPage, rect, m_pPdfDocument);

	if(szFieldName && szFieldName[0])
		m_pSignatureField->SetFieldName(PdfString(szFieldName));

	if(szSubFilter && szSubFilter[0])
	{
		m_pSignatureField->EnsureSignatureObject();
		PdfDictionary& sigDict = m_pSignatureField->GetSignatureObject()->GetDictionary();
		PdfName subFilterKey("SubFilter");
		if(sigDict.HasKey(subFilterKey))
			sigDict.RemoveKey(subFilterKey);
		sigDict.AddKey(subFilterKey, PdfName(szSubFilter));
	}

	SignatureLabels labels;

	LOG_DBG((0, "InitSignature", "PdfSignatureField OK"));

	//if(width * height == 0)
	//	m_pSignatureField->SetHighlightingMode(ePdfHighlightingMode_None);
	
	if(szReason && szReason[0])
	{
		SetSignatureDictString(m_pSignatureField, "Reason", PdfString(szReason));
		if(szReasonLabel)
			labels.reason = szReasonLabel;
	}
	
	LOG_DBG((0, "InitSignature", "szReason OK"));

	if(szLocation && szLocation[0])
	{
		SetSignatureDictString(m_pSignatureField, "Location", PdfString(szLocation));
		if(szLocationLabel)
			labels.location = szLocationLabel;
	}

	LOG_DBG((0, "InitSignature", "szLocation OK"));

	PdfDate now;
	m_pSignatureField->SetSignatureDate(now);
	
	LOG_DBG((0, "InitSignature", "Date OK"));

	if(szName && szName[0])
	{
		SetSignatureDictString(m_pSignatureField, "Name", PdfString(szName));
		if(szNameLabel)
			labels.name = szNameLabel;
	}
	
	LOG_DBG((0, "InitSignature", "szName OK"));

	LOG_DBG((0, "InitSignature", "SINGNATURE_SIZE OK"));

	//if((szImagePath && szImagePath[0]) || (szDescription && szDescription[0]))
	if(width * height > 0)
	{
		try
		{
            //m_pSignatureField->SetFontSize(5);
			SetSignatureAppearance(m_pSignatureField, m_pPdfDocument, szImagePath, szDescription, labels);
			LOG_DBG((0, "InitSignature", "SetAppearance OK"));
		}
		catch( PdfError & error ) 
		{
			LOG_ERR((0, "InitSignature", "SetAppearance error: %s, %s", PdfError::ErrorMessage(error.GetError()), error.what()));			
		}
		catch( PdfError * perror ) 
		{
			LOG_ERR((0, "InitSignature", "SetAppearance error2: %s, %s", PdfError::ErrorMessage(perror->GetError()), perror->what()));			
		}
		catch(std::exception& ex)
		{
			LOG_ERR((0, "InitSignature", "SetAppearance std exception, %s", ex.what()));			
		}
		catch(std::exception* pex)
		{
			LOG_ERR((0, "InitSignature", "SetAppearance std exception2, %s", pex->what()));			
		}
		catch(...)
		{
			LOG_ERR((0, "InitSignature", "SetAppearance unknown error"));			
		}
	}
	

	if(szGraphometricData && szGraphometricData[0])
		SetSignatureGraphometricData(m_pSignatureField, PdfString("Aruba_Sign_Biometric_Data"), PdfString(szGraphometricData), PdfString(szVersion));

	LOG_DBG((0, "InitSignature", "szGraphometricData OK"));


	//	// crea il nuovo doc con il campo di firma
	//	int fulllen = m_actualLen * 3 + SINGNATURE_SIZE * 2;
	//	m_pMainDocbuffer = new char[fulllen];
	//	PdfOutputDevice pdfOutDevice(m_pMainDocbuffer, fulllen);	
	//	m_pPdfDocument->Write(&pdfOutDevice);
	//	int mainDoclen = pdfOutDevice.GetLength();
	
    LOG_DBG((0, "InitSignature", "m_actualLen %d", m_actualLen));
	// crea il nuovo doc con il campo di firma
	int fulllen = m_actualLen * 2 + SINGNATURE_SIZE * 2 + (szGraphometricData ? (strlen(szGraphometricData) + strlen(szVersion) + 100) : 0);

	

	// beacon segnaposto per /Contents; scritto nel campo prima della serializzazione.
	int signBufLen = fulllen;
	m_pSignDocbuffer = new char[signBufLen];
	m_pFinalOutDevice = new PdfOutputDevice(m_pSignDocbuffer, signBufLen);
	m_pSignOutputDevice = new PdfSignOutputDevice(m_pFinalOutDevice);
	m_pSignOutputDevice->SetSignatureSize(SINGNATURE_SIZE);
	m_pSignatureField->SetSignature(*m_pSignOutputDevice->GetSignatureBeacon());

	LOG_DBG((0, "InitSignature", "beacon OK %d", SINGNATURE_SIZE));

	int mainDoclen = 0;
	m_pMainDocbuffer = NULL;
	while (!m_pMainDocbuffer) {
		try{
            LOG_DBG((0, "InitSignature", "fulllen %d", fulllen));
			m_pMainDocbuffer = new char[fulllen];
			PdfOutputDevice pdfOutDevice(m_pMainDocbuffer, fulllen);
			m_pPdfDocument->Write(&pdfOutDevice);
			mainDoclen = pdfOutDevice.GetLength();
		}
		catch (::PoDoFo::PdfError err) {
			if(m_pMainDocbuffer) {
				delete m_pMainDocbuffer;
				m_pMainDocbuffer = NULL;
			}
            
            LOG_DBG((0, "PdfError", "what %s", err.what()));
			fulllen *= 2;
		}
	}
	
	LOG_DBG((0, "InitSignature", "m_pMainDocbuffer %d", fulllen));

	if(fulllen != signBufLen)
	{
		delete m_pSignOutputDevice;
		delete m_pFinalOutDevice;
		delete[] m_pSignDocbuffer;

		signBufLen = fulllen;
		m_pSignDocbuffer = new char[signBufLen];
		m_pFinalOutDevice = new PdfOutputDevice(m_pSignDocbuffer, signBufLen);
		m_pSignOutputDevice = new PdfSignOutputDevice(m_pFinalOutDevice);
		m_pSignOutputDevice->SetSignatureSize(SINGNATURE_SIZE);
	}

	LOG_DBG((0, "InitSignature", "buffers OK %d", fulllen));

	// Scrive il documento reale
	m_pSignOutputDevice->Write(m_pMainDocbuffer, mainDoclen);

	LOG_DBG((0, "InitSignature", "Write OK %d", mainDoclen));

	m_pSignOutputDevice->AdjustByteRange();

	LOG_DBG((0, "InitSignature", "AdjustByteRange OK"));

}

// In PoDoFo 0.9 sono tre passi separati
long PdfSignatureGenerator::SignDocument(const CieSignCallback& signCallback,
                                         UUCByteArray& signedPdf)
{
	UUCByteArray toSign;
	GetBufferForSignature(toSign);

	UUCByteArray signature;
	long nRes = signCallback(toSign.getContent(), toSign.getLength(), signature);
	if(nRes != 0)
		return nRes;

	SetSignature((const char*)signature.getContent(), signature.getLength());
	GetSignedPdf(signedPdf);

	return 0;
}

void PdfSignatureGenerator::GetBufferForSignature(UUCByteArray& toSign)
{
	//int fulllen = m_actualLen * 2 + SINGNATURE_SIZE * 2;
	int len = m_pSignOutputDevice->GetLength() * 2;
	
	char* buffer = new char[len];
	
	m_pSignOutputDevice->Seek(0);
	
	int nRead = m_pSignOutputDevice->ReadForSignature(buffer, len);
	if(nRead == -1)
		throw nRead;
	
	toSign.append((BYTE*)buffer, nRead);
	
	delete buffer;
}

void PdfSignatureGenerator::SetSignature(const char* signature, int len)
{
	PdfData signatureData(signature, len);
	m_pSignOutputDevice->SetSignature(signatureData);
}

void PdfSignatureGenerator::GetSignedPdf(UUCByteArray& signedPdf)
{
	int finalLength = m_pSignOutputDevice->GetLength();
	char* szSignedPdf = new char[finalLength];
	
	m_pSignOutputDevice->Seek(0);
	int nRead = m_pSignOutputDevice->Read(szSignedPdf, finalLength);
	
	signedPdf.append((BYTE*)szSignedPdf, nRead);
	
	delete szSignedPdf;
}

const double PdfSignatureGenerator::getWidth(int pageIndex) {
	if (m_pPdfDocument) {
		PdfPage* pPage = m_pPdfDocument->GetPage(pageIndex);
		return pPage->GetPageSize().GetWidth();
	}
	return 0;
}

const double PdfSignatureGenerator::getHeight(int pageIndex) {
	if (m_pPdfDocument) {
		PdfPage* pPage = m_pPdfDocument->GetPage(pageIndex);
		return pPage->GetPageSize().GetHeight();
	}
	return 0;
}

const double PdfSignatureGenerator::lastSignatureY(int left, int bottom) {
	if(!m_pPdfDocument)
		return -1;
	/// Find the document catalog dictionary
	const PdfObject *const trailer = m_pPdfDocument->GetTrailer();
	if (! trailer->IsDictionary())
		return -1;
	const PdfObject *const catalogRef =	trailer->GetDictionary().GetKey(PdfName("Root"));
	if (catalogRef==0 || ! catalogRef->IsReference())
		return -2;//throw std::invalid_argument("Invalid /Root entry");
	const PdfObject *const catalog =
		m_pPdfDocument->GetObjects().GetObject(catalogRef->GetReference());
	if (catalog==0 || !catalog->IsDictionary())
		return -3;//throw std::invalid_argument("Invalid or non-dictionary
	//referenced by /Root entry");
	
	/// Find the Fields array in catalog dictionary
	const PdfObject *acroFormValue = catalog->GetDictionary().GetKey(PdfName("AcroForm"));
	if (acroFormValue == 0)
		return bottom;
	if (acroFormValue->IsReference())
		acroFormValue = m_pPdfDocument->GetObjects().GetObject(acroFormValue->GetReference());
	
	if (!acroFormValue->IsDictionary())
		return bottom;
	
	const PdfObject *fieldsValue = acroFormValue->GetDictionary().GetKey(PdfName("Fields"));
	if (fieldsValue == 0)
		return bottom;
	
	if (fieldsValue->IsReference())
		fieldsValue = m_pPdfDocument->GetObjects().GetObject(acroFormValue->GetReference());
	
	if (!fieldsValue->IsArray())
		return bottom;
	
	vector<const PdfObject*> signatureVector;
	
	/// Verify if each object of the array is a signature field
	const PdfArray &array = fieldsValue->GetArray();
	
	int minY = bottom;
	
	for (unsigned int i=0; i<array.size(); i++) {
		const PdfObject * pObj = m_pPdfDocument->GetObjects().GetObject(array[i].GetReference());
		if (IsSignatureField(m_pPdfDocument, pObj)) {
			const PdfObject *const keyRect = pObj->GetDictionary().GetKey(PdfName("Rect"));
			if (keyRect == 0) {
				return bottom;
			}
			PdfArray rectArray = keyRect->GetArray();
			PdfRect rect;
			rect.FromArray(rectArray);
			
			if (rect.GetLeft() == left) {
				minY = (rect.GetBottom() <= minY && rect.GetBottom()!=0) ? rect.GetBottom()-85 : minY;
			}
		}
	}
	return minY;
}

bool PdfSignatureGenerator::IsSignatureField(const PdfMemDocument* pDoc, const PdfObject *const pObj)
{
	if (pObj == 0) return false;
	
	if (!pObj->IsDictionary())
		return false;
	
	const PdfObject *const keyFTValue = pObj->GetDictionary().GetKey(PdfName("FT"));
	if (keyFTValue == 0)
		return false;
	
	string value;
	keyFTValue->ToString(value);
	if (value != "/Sig")
		return false;
	
	const PdfObject *const keyVValue = pObj->GetDictionary().GetKey(PdfName("V"));
	if (keyVValue == 0)
		return false;
	
	const PdfObject *const signature = pDoc->GetObjects().GetObject(keyVValue->GetReference());
	if (signature->IsDictionary())
		return true;
	else
		return false;
}

// ---------------------------------------------------------------------------
// Questi sono i sostituti dei due metodi che esistevano solo nel fork PoDoFo
// presente in cie_sign_sdk/Dependencies/podofo.
// Qui vengono reimplementati (sorgente del fork non disponibile) fuori dalla libreria.
// ---------------------------------------------------------------------------

// Scriveva nel dizionario della firma un sotto-dizionario con i dati
// biometrici cifrati:
//
//     /<rsName> << /Encrypted_Graphometric_Data (...) /Version (...) >>
static void SetSignatureGraphometricData(PdfSignatureField* pField,
                                         const PdfString& rsName,
                                         const PdfString& rsData,
                                         const PdfString& rsVersion)
{
	pField->EnsureSignatureObject();
	PdfDictionary& sigDict = pField->GetSignatureObject()->GetDictionary();

	PdfName key(rsName.GetString());
	if(sigDict.HasKey(key))
		sigDict.RemoveKey(key);

	PdfDictionary bio;
	bio.AddKey(PdfName("Encrypted_Graphometric_Data"), PdfObject(rsData));
	bio.AddKey(PdfName("Version"), PdfObject(rsVersion));

	sigDict.AddKey(key, PdfObject(bio));
}

// Disegna una riga "etichetta valore" solo se la chiave e' presente nel
// dizionario della firma, facendo avanzare la posizione verticale.
static void DrawSignatureKey(PdfPainter& painter, const PdfDictionary& sigDict,
                             const char* szKey, const std::string& label,
                             double dX, double& dY, double dLineHeight,
                             const PdfEncoding* pEncoding)
{
	if(label.empty() || !sigDict.HasKey(PdfName(szKey)))
		return;

	const PdfObject* pValue = sigDict.GetKey(PdfName(szKey));
	if(pValue == NULL || !pValue->IsString())
		return;

	std::string line(label);
	line += " ";
	line += pValue->GetString().GetString();

	dY -= dLineHeight;
	painter.DrawText(dX, dY, PdfString(line.c_str(), pEncoding));
}

// Costruisce l'apparenza a strati prevista da Adobe per le firme visibili:
//
//     /AP << /N -> XObject esterno
//                    disegna -> FRM
//                                 disegna -> n0  (sfondo dichiarato vuoto)
//                                         -> n2  (immagine + testo)
//
// I prefissi degli XObject sono senza il "#" iniziale usato in precedenza
static void SetSignatureAppearance(PdfSignatureField* pField,
                                   PdfMemDocument* pDoc,
                                   const char* szImagePath,
                                   const char* szDescription,
                                   const SignatureLabels& labels)
{
	if(pField == NULL || pDoc == NULL)
		PODOFO_RAISE_ERROR(ePdfError_InvalidHandle);

	pField->SetBackgroundColorTransparent();

	const PdfRect rWidget = pField->GetWidgetAnnotation()->GetRect();
	const PdfRect rBox(0.0, 0.0, rWidget.GetWidth(), rWidget.GetHeight());

	PdfVecObjects* pObjects = pField->GetFieldObject()->GetOwner();

	PdfImage image(pObjects, "Im");
	PdfImage* pImage = NULL;
	if(szImagePath != NULL && szImagePath[0] != '\0')
	{
		image.SetInterpolate(true);
		image.LoadFromPng(szImagePath);

		// PdfImage aggiunge un /BBox che alcuni lettori
		// interpretano male dentro un XObject di apparenza.
		image.GetObject()->GetDictionary().RemoveKey(PdfName("BBox"));
		pImage = &image;
	}

	PdfXObject n0(PdfRect(0.0, 0.0, 100.0, 100.0), pDoc, "n0");
	const char szBlank[] = "% DSBlank\n";
	n0.GetObject()->GetStream()->Set(szBlank, sizeof(szBlank) - 1);

	PdfXObject n2(rBox, pDoc, "n2");
	{
		PdfPainter painter;
		painter.SetPage(&n2);

		if(pImage != NULL)
		{
			const double dScaleX = rBox.GetWidth() / pImage->GetWidth();
			const double dScaleY = rBox.GetHeight() / pImage->GetHeight();
			painter.DrawImage(0.0, 0.0, pImage, dScaleX, dScaleY);
		}

		const PdfEncoding* pEncoding = PdfEncodingFactory::GlobalWinAnsiEncodingInstance();
		PdfFont* pFont = pDoc->CreateFont("Helvetica", false, false, false, pEncoding,
		                                  PdfFontCache::eFontCreationFlags_AutoSelectBase14, true);
		pFont->SetFontSize(10.0f);
		painter.SetFont(pFont);

		const double dLineHeight = 10.0;
		const double dX = 5.0;
		double dY = rBox.GetHeight();

		if(szDescription != NULL && szDescription[0] != '\0')
		{
			dY -= dLineHeight;
			painter.DrawText(dX, dY, PdfString(szDescription, pEncoding));
		}

		PdfObject* pSigObj = pField->GetSignatureObject();
		if(pSigObj != NULL && pSigObj->IsDictionary())
		{
			const PdfDictionary& sigDict = pSigObj->GetDictionary();
			DrawSignatureKey(painter, sigDict, "Name", labels.name, dX, dY, dLineHeight, pEncoding);
			DrawSignatureKey(painter, sigDict, "Reason", labels.reason, dX, dY, dLineHeight, pEncoding);
			DrawSignatureKey(painter, sigDict, "Location", labels.location, dX, dY, dLineHeight, pEncoding);
		}

		painter.FinishPage();
	}

	PdfXObject frm(rBox, pDoc, "FRM");
	{
		PdfPainter painter;
		painter.SetPage(&frm);
		painter.DrawXObject(0.0, 0.0, &n0, 1.0, 1.0);
		painter.DrawXObject(0.0, 0.0, &n2, 1.0, 1.0);
		painter.FinishPage();
	}

	PdfXObject ap(rBox, pDoc);
	{
		PdfPainter painter;
		painter.SetPage(&ap);
		painter.DrawXObject(0.0, 0.0, &frm, 1.0, 1.0);
		painter.FinishPage();
	}

	PdfDictionary apDict;
	apDict.AddKey(PdfName("N"), PdfObject(ap.GetObject()->Reference()));
	pField->GetFieldObject()->GetDictionary().AddKey(PdfName("AP"), PdfObject(apDict));
}

#endif // !CIE_PODOFO_MODERN

#if CIE_PODOFO_MODERN

// ===========================================================================
// Backend per PoDoFo 0.10 e 1.x.
//
// Il modello di firma e' completamente diverso da quello della 0.9:
// PdfSignOutputDevice non esiste piu' e al suo posto c'e' PoDoFo::SignDocument().
// Riceve un PdfSigner e lo invoca dall'interno.
// ===========================================================================

namespace
{
    // Accumula i byte del documento e delega la firma alla callback.
    class CallbackSigner final : public PdfSigner
    {
    public:
        CallbackSigner(const CieSignCallback& cb, size_t reservedSize)
            : m_cb(cb), m_reserved(reservedSize), m_result(0) { }

        void Reset() override { m_buffer.clear(); }

        void AppendData(const bufferview& data) override
        {
            m_buffer.insert(m_buffer.end(), data.data(), data.data() + data.size());
        }

        void ComputeSignature(charbuff& contents, bool dryrun) override
        {
            if (dryrun)
            {
                // Dimensiona /Contents.
                contents.resize(m_reserved);
                return;
            }

            UUCByteArray signature;
            m_result = m_cb((const unsigned char*)m_buffer.data(),
                            (unsigned long)m_buffer.size(), signature);
            if (m_result != 0)
                return;

            if (signature.getLength() > m_reserved)
            {
                m_result = DISIGON_ERROR_UNEXPECTED;
                return;
            }

            contents.assign((const char*)signature.getContent(),
                            (const char*)signature.getContent() + signature.getLength());
        }

        std::string GetSignatureSubFilter() const override { return m_subFilter; }
        std::string GetSignatureType() const override { return "Sig"; }

        void SetSubFilter(const std::string& sf) { if (!sf.empty()) m_subFilter = sf; }
        long GetResult() const { return m_result; }

    private:
        const CieSignCallback& m_cb;
        size_t m_reserved;
        long m_result;
        std::string m_subFilter = "adbe.pkcs7.detached";
        std::vector<char> m_buffer;
    };
}

PdfSignatureGenerator::PdfSignatureGenerator()
: m_pPdfDocument(NULL), m_pSignature(NULL)
{
    PoDoFo::PdfCommon::SetMaxLoggingSeverity(PdfLogSeverity::None);
}

PdfSignatureGenerator::~PdfSignatureGenerator()
{
    if (m_pPdfDocument)
        delete m_pPdfDocument;
}

int PdfSignatureGenerator::Load(const char* pdf, int len)
{
    if (m_pPdfDocument)
        delete m_pPdfDocument;

    try
    {
        m_pPdfDocument = new PdfMemDocument();
        m_pPdfDocument->LoadFromBuffer(bufferview(pdf, len));

        // SignDocument() fa un aggiornamento incrementale
        // il device di uscita già avere contenere il documento di partenza.
        m_originalPdf.assign(pdf, pdf + len);
        m_actualLen = len;

        return PDFVerifier::GetNumberOfSignatures(m_pPdfDocument);
    }
    catch (::PoDoFo::PdfError&)
    {
        return -2;
    }
    catch (...)
    {
        return -1;
    }
}

void PdfSignatureGenerator::AddFont(const char* szFontName, const char* szFontPath)
{
    // Dalla 0.10 i font si cercano per nome nel gestore del documento;
    (void)szFontPath;
    m_pPdfDocument->GetFonts().SearchFont(szFontName);
}

void PdfSignatureGenerator::InitSignature(int pageIndex, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter)
{
    InitSignature(pageIndex, 0, 0, 0, 0, szReason, szReasonLabel, szName, szNameLabel, szLocation, szLocationLabel, szFieldName, szSubFilter);
}

void PdfSignatureGenerator::InitSignature(int pageIndex, float left, float bottom, float width, float height, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter)
{
    InitSignature(pageIndex, left, bottom, width, height, szReason, szReasonLabel, szName, szNameLabel, szLocation, szLocationLabel, szFieldName, szSubFilter, NULL, NULL, NULL, NULL);
}

void PdfSignatureGenerator::InitSignature(int pageIndex, float left, float bottom, float width, float height, const char* szReason, const char* szReasonLabel, const char* szName, const char* szNameLabel, const char* szLocation, const char* szLocationLabel, const char* szFieldName, const char* szSubFilter, const char* szImagePath, const char* szDescription, const char* szGraphometricData, const char* szVersion)
{
    auto& page = m_pPdfDocument->GetPages().GetPageAt(pageIndex);
    Rect cropBox = page.GetCropBox();

    double left0   = left   * cropBox.Width;
    double bottom0 = cropBox.Height - (bottom * cropBox.Height);
    double width0  = width  * cropBox.Width;
    double height0 = height * cropBox.Height;

    Rect rect(left0, bottom0, width0, height0);

    m_pSignature = &page.CreateField<PdfSignature>(
        szFieldName && szFieldName[0] ? szFieldName : "Signature1", rect);

    // Il dizionario /V non viene creato da solo: senza questa chiamata ogni
    // SetSignature* lancia InvalidHandle.
    m_pSignature->EnsureValueObject();

    if (szReason && szReason[0])
        m_pSignature->SetSignatureReason(PdfString(szReason));

    if (szLocation && szLocation[0])
        m_pSignature->SetSignatureLocation(PdfString(szLocation));

    if (szName && szName[0])
        m_pSignature->SetSignerName(PdfString(szName));

    m_pSignature->SetSignatureDate(PdfDate::LocalNow());

    if (width * height > 0)
    {
        try
        {
            auto xobj = m_pPdfDocument->CreateXObjectForm(Rect(0, 0, rect.Width, rect.Height));
            PdfPainter painter;
            painter.SetCanvas(*xobj);

            // Aggira un errore di Adobe Reader quando il flusso contiene un
            // solo oggetto che fa Save()/Restore() per conto suo.
            painter.Save();
            painter.Restore();

            if (szImagePath && szImagePath[0])
            {
                auto image = m_pPdfDocument->CreateImage();
                image->Load(szImagePath);
                painter.DrawImage(*image, 0, 0,
                                  rect.Width / image->GetWidth(),
                                  rect.Height / image->GetHeight());
            }

            // SearchFont() dipende dai font di sistema e può non trovare
            // nulla: uso i base14 (garantiti senza altri file).
            PdfFont& font = m_pPdfDocument->GetFonts().GetStandard14Font(
                PdfStandard14FontType::Helvetica);
            {
                const double lineHeight = 10.0;
                double y = rect.Height - lineHeight;
                painter.TextState.SetFont(font, 10.0);

                if (szDescription && szDescription[0])
                {
                    painter.DrawText(szDescription, 5.0, y);
                    y -= lineHeight;
                }
                if (szName && szName[0])
                {
                    painter.DrawText(std::string(szNameLabel ? szNameLabel : "") + " " + szName, 5.0, y);
                    y -= lineHeight;
                }
                if (szReason && szReason[0])
                {
                    painter.DrawText(std::string(szReasonLabel ? szReasonLabel : "") + " " + szReason, 5.0, y);
                    y -= lineHeight;
                }
                if (szLocation && szLocation[0])
                {
                    painter.DrawText(std::string(szLocationLabel ? szLocationLabel : "") + " " + szLocation, 5.0, y);
                }
            }

            painter.FinishDrawing();

#if PODOFO_VERSION_MAJOR >= 1
            // Nella 1.x SetAppearanceStream sta sull'annotazione, non sul campo.
            m_pSignature->MustGetWidget().SetAppearanceStream(*xobj);
#else
            m_pSignature->SetAppearanceStream(*xobj);
#endif
        }
        catch (PdfError& error)
        {
            LOG_ERR((0, "InitSignature", "appearance error: %s", error.what()));
        }
        catch (...)
        {
            LOG_ERR((0, "InitSignature", "appearance: errore sconosciuto"));
        }
    }

    if (szGraphometricData && szGraphometricData[0])
    {
        PdfObject* pValue = m_pSignature->GetDictionary().FindKey("V");
        if (pValue != NULL)
        {
            PdfDictionary bio;
            bio.AddKey(PdfName("Encrypted_Graphometric_Data"), PdfObject(PdfString(szGraphometricData)));
            bio.AddKey(PdfName("Version"), PdfObject(PdfString(szVersion ? szVersion : "")));
            pValue->GetDictionary().AddKey(PdfName("Aruba_Sign_Biometric_Data"), PdfObject(bio));
        }
    }

    m_subFilter = szSubFilter ? szSubFilter : "";
}

long PdfSignatureGenerator::SignDocument(const CieSignCallback& signCallback,
                                         UUCByteArray& signedPdf)
{
    if (m_pPdfDocument == NULL || m_pSignature == NULL)
        return DISIGON_ERROR_UNEXPECTED;

    try
    {
        // Aggiornamento incrementale: il device parte dal documento originale
        // e il costruttore a un argomento si posiziona in coda.
        charbuff out;
        out.assign(m_originalPdf.begin(), m_originalPdf.end());
        BufferStreamDevice device(out);

        CallbackSigner signer(signCallback, SINGNATURE_SIZE);
        signer.SetSubFilter(m_subFilter);

        PoDoFo::SignDocument(*m_pPdfDocument, device, signer, *m_pSignature);

        if (signer.GetResult() != 0)
            return signer.GetResult();

        signedPdf.append((BYTE*)out.data(), (unsigned long)out.size());
        return 0;
    }
    catch (::PoDoFo::PdfError& err)
    {
        LOG_ERR((0, "SignDocument", "PdfError: %s", err.what()));
        return DISIGON_ERROR_UNEXPECTED;
    }
}

const double PdfSignatureGenerator::getWidth(int pageIndex)
{
    return m_pPdfDocument->GetPages().GetPageAt(pageIndex).GetRect().Width;
}

const double PdfSignatureGenerator::getHeight(int pageIndex)
{
    return m_pPdfDocument->GetPages().GetPageAt(pageIndex).GetRect().Height;
}

#endif // CIE_PODOFO_MODERN
