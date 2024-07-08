/*
 * SPDX-License-Identifier: BSD-3-Clause
 */
 
#pragma once
#include "definitions.h"

#define ERR_INDEX_OUT_OF_BOUND    0xC0001001L

class UUCByteArray  
{
public:
	UUCByteArray(const BYTE* pbtContent, const unsigned long unLen);
	UUCByteArray(const UUCByteArray& blob);
	UUCByteArray(const char* szHexString);
	UUCByteArray(const unsigned long nLen);
	UUCByteArray();

	void load(const char* szHexString);

	virtual ~UUCByteArray();

	const BYTE* getContent() const;
	unsigned long getLength() const;
	
	void reverse();
	void append(const BYTE btVal);
	void append(const BYTE* pbtVal, const unsigned int nLen);
	void append(const UUCByteArray& val);
	void append(const char* szHexString);
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
