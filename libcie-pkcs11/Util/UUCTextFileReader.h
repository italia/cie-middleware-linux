/* UUCTextFile.h: interface for the UUCTextFile class.
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

#if !defined(AFX_UUCTEXTFILE_H__CD3660A5_B4C5_4CD4_99AC_69AC96D1460F__INCLUDED_)
#define AFX_UUCTEXTFILE_H__CD3660A5_B4C5_4CD4_99AC_69AC96D1460F__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <stdio.h>
#include "UUCByteArray.h"

class UUCTextFileReader
{
public:
    UUCTextFileReader(const char* szFilePath);
    virtual ~UUCTextFileReader();

    long readLine(char* szLine, unsigned long nLen);// throw (long);
    long readLine(UUCByteArray& line);
private:

    FILE* m_pf;
};

#endif // !defined(AFX_UUCTEXTFILE_H__CD3660A5_B4C5_4CD4_99AC_69AC96D1460F__INCLUDED_)
