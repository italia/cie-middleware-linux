/*
 *  SPDX-License-Identifier: BSD-3-Clause
 */
 
#pragma once


#define ERR_INDEX_OUT_OF_BOUND    0xC0001001L
#define ERROR_UNABLE_TO_ALLOCATE  0xC0001002L
#define S_OK    0
#ifndef BYTE
#define BYTE unsigned char
#endif

class UUCByteArray  
{
public:
	UUCByteArray(const BYTE* pbtContent, const unsigned long unLen);
	UUCByteArray(const UUCByteArray& blob);
	UUCByteArray(const char* szHexString);
	UUCByteArray(const unsigned long nLen);
	UUCByteArray();

	long load(const char* szHexString);

	virtual ~UUCByteArray();

	const BYTE* getContent() const;
	unsigned long getLength() const;
	
	long reverse();
	long append(const BYTE btVal);
	long append(const BYTE* pbtVal, const unsigned long nLen);
	long append(const UUCByteArray& val);
	long append(const char* szHexString);
	BYTE get(const unsigned int index) const;// throw(long);
	void set(const unsigned int index, const BYTE btVal);// throw (long);
	BYTE operator [] (const unsigned int index) const;// throw(long);
	void remove(const unsigned int index);// throw(long);
	void removeAll();
	//void toHexString(char* szHex) const;	
	const char* toHexString();
	const char* toHexString(int nSize);

private:
	BYTE* m_pbtContent;
	unsigned long m_unLen;
	unsigned long m_nCapacity;
	char* m_szHex;
};
