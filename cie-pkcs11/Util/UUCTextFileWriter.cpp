/* UUCTextFileWriter.cpp: implementation of the UUCTextFileWriter class.
*
*  Copyright (c) 2000-2018 by Ugo Chirico - http://www.ugochirico.com
*  All Rights Reserved
*
*  This program is free software; you can redistribute it and/or modify
*  it under the terms of the GNU Lesser General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with this program; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/

#include "UUCTextFileWriter.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

UUCTextFileWriter::UUCTextFileWriter(const char* szFilePath, bool bAppend /*= false*/)
{
	if(bAppend)
		m_pf = fopen(szFilePath, "a+t");
	else 
		m_pf = fopen(szFilePath, "wt");
	
	if(!m_pf)
		throw (long)ERROR_FILE_NOT_FOUND;
}

UUCTextFileWriter::~UUCTextFileWriter()
{
	fclose(m_pf);
}

long UUCTextFileWriter::writeLine(const char* szLine)
{
	if(fprintf(m_pf, "%s\n", szLine) < 0)
		return -1;	

	fflush(m_pf);

	return 0;
}

long UUCTextFileWriter::writeLine(const UUCByteArray& byteArray)
{
	char* pszLine = new char[byteArray.getLength() + 1];
	memcpy(pszLine, byteArray.getContent(), byteArray.getLength());

	if(fprintf(m_pf, "%s\n", pszLine) < 0)
	{
		delete[] pszLine;
#ifdef WIN32
		return GetLastError();
#else
		return -1;
#endif
	}

	delete[] pszLine;
	fflush(m_pf);

	return 0;
}

