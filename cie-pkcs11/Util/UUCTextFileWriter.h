/* UUCTextFileWriter.h: interface for the UUCTextFileWriter class.
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

#if !defined(AFX_UUCTEXTFILEWRITER_H__F64974CA_4F95_4200_B7AE_4A53FB004B75__INCLUDED_)
#define AFX_UUCTEXTFILEWRITER_H__F64974CA_4F95_4200_B7AE_4A53FB004B75__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
#include <stdio.h>
#include "UUCByteArray.h"

class UUCTextFileWriter  
{
public:
	UUCTextFileWriter(const char* szFilePath, bool bAppend = false);
	virtual ~UUCTextFileWriter();

	long writeLine(const char* szLine);
	long writeLine(const UUCByteArray& byteArray);

private:
	FILE* m_pf;
};

#endif // !defined(AFX_UUCTEXTFILEWRITER_H__F64974CA_4F95_4200_B7AE_4A53FB004B75__INCLUDED_)
