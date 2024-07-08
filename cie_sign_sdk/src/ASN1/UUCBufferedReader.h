/* BufferedReader.h: interface for the CBufferedReader class.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
 
#pragma once
#include <stdio.h>
#include "UUCByteArray.h"

class UUCBufferedReader  
{
public:
	unsigned int getPosition();
	void setPosition(unsigned int index);
	//UUCBufferedReader(const char* szFilePath);	
	UUCBufferedReader(const UUCByteArray& buffer);
	UUCBufferedReader(const BYTE* pbtBuffer, int len);
	
	virtual ~UUCBufferedReader();

	unsigned int read(BYTE* pbtBuffer, unsigned int  nLen);
	unsigned int read(UUCByteArray& byteArray);

	void mark();
	void reset();	
	void releaseMark();

private:
	//unsigned int readNext();

	//FILE* m_pf;
	BYTE* m_pbtBuffer;
	//UUCByteArray* m_pByteArray;
	unsigned int m_nBufPos;
	unsigned int m_nBufLen;
	unsigned int m_nIndex;
	bool m_bEOF;

	unsigned int* m_pnStack;
	unsigned int  m_nStackSize;
	int  m_nTop;
};
