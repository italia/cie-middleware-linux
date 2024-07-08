/*
 * SPDX-License-Identifier: BSD-3-Clause
 */
 

#ifndef _ASN1EXCEPTION_H
#define _ASN1EXCEPTION_H

class CASN1Exception
{
public:
	CASN1Exception(const char* lpszMsg)
		: m_lpszMsg(lpszMsg)
	{
		
	};

	virtual ~CASN1Exception(){};
		
	virtual bool GetErrorMessage(char* lpszError, UINT nMaxError)
	{
		if(nMaxError < strlen(m_lpszMsg))
			return false;

		strcpy(lpszError, m_lpszMsg);

		return true;
	};


public:
	const char* m_lpszMsg;
};

class CASN1ParsingException : public CASN1Exception
{
public:
	CASN1ParsingException()
		: CASN1Exception("Bad ASN1Object parsed")
	{};
		
	virtual ~CASN1ParsingException(){};
	
};


class CASN1ObjectNotFoundException: public CASN1Exception
{
public:
	CASN1ObjectNotFoundException(const char* lpszClass)
		:CASN1Exception(lpszClass)
	{};
		
	virtual ~CASN1ObjectNotFoundException(){};
};

class CASN1BadObjectIdException : public CASN1Exception
{
public:
	CASN1BadObjectIdException(const char* strClass)
		: CASN1Exception(strClass)
	{};
		
	virtual ~CASN1BadObjectIdException(){};
};

/*
class CFileNotFoundException : public CASN1Exception
{
public:
	CFileNotFoundException(LPCTSTR lpszFile)
		: CASN1Exception(lpszFile)
	{};
};
*/
class CBadContentTypeException : public CASN1Exception
{
public:
	CBadContentTypeException()
		: CASN1Exception("Bad Content Type")
	{};
		
	virtual ~CBadContentTypeException(){};
};


#endif //_ASN1EXCEPTION_H
