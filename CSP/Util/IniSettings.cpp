#include "../StdAfx.h"
#include "IniSettings.h"
#include "../Crypto/Base64.h"
#include <sstream>
#include <stdio.h>
#include <string.h>

std::vector<IniSettings*> _iniSettings;

#ifndef _WIN32
#define MAX_LINE_LENGTH    200
/*****************************************************************
* Function:     read_line()
* Arguments:    <FILE *> fp - a pointer to the file to be read from
*               <char *> bp - a pointer to the copy buffer
* Returns:      TRUE if successful FALSE otherwise
******************************************************************/
int read_line(FILE *fp, char *bp)
{   char c = '\0';
    int i = 0;
    /* Read one line from the source file */
    while( (c = getc(fp)) != '\n' )
    {   if( c == EOF )         /* return FALSE on unexpected EOF */
            return(0);
        bp[i++] = c;
    }
    bp[i] = '\0';
    return(1);
}

/************************************************************************
* Function:     get_private_profile_int()
* Arguments:    <char *> section - the name of the section to search for
*               <char *> entry - the name of the entry to find the value of
*               <int> def - the default value in the event of a failed read
*               <char *> file_name - the name of the .ini file to read from
* Returns:      the value located at entry
*************************************************************************/
int GetPrivateProfileInt(const char *section, const char *entry, int def, const char *file_name)
{   FILE *fp = fopen(file_name,"r");
    char buff[MAX_LINE_LENGTH];
    char *ep;
    char t_section[MAX_LINE_LENGTH];
    char value[6];
    int len = strlen(entry);
    int i;
    if( !fp ) return(0);
    sprintf(t_section,"[%s]",section); /* Format the section name */
    /*  Move through file 1 line at a time until a section is matched or EOF */
    do
    {   if( !read_line(fp,buff) )
        {   fclose(fp);
            return(def);
        }
    } while( strcmp(buff,t_section) );
    /* Now that the section has been found, find the entry.
     * Stop searching upon leaving the section's area. */
    do
    {   if( !read_line(fp,buff) || buff[0] == '\0' )
        {   fclose(fp);
            return(def);
        }
    }  while( strncmp(buff,entry,len) );
    ep = strrchr(buff,'=');    /* Parse out the equal sign */
    ep++;
    if( !strlen(ep) )          /* No setting? */
        return(def);
    /* Copy only numbers fail on characters */

    for(i = 0; isdigit(ep[i]); i++ )
        value[i] = ep[i];
    value[i] = '\0';
    fclose(fp);                /* Clean up and return the value */
    return(atoi(value));
}

/**************************************************************************
* Function:     get_private_profile_string()
* Arguments:    <char *> section - the name of the section to search for
*               <char *> entry - the name of the entry to find the value of
*               <char *> def - default string in the event of a failed read
*               <char *> buffer - a pointer to the buffer to copy into
*               <int> buffer_len - the max number of characters to copy
*               <char *> file_name - the name of the .ini file to read from
* Returns:      the number of characters copied into the supplied buffer
***************************************************************************/
int GetPrivateProfileStringA(const char *section, const char *entry, const char *def,
    char *buffer, int buffer_len, const char *file_name)
{   FILE *fp = fopen(file_name,"r");
    char buff[MAX_LINE_LENGTH];
    char *ep;
    char t_section[MAX_LINE_LENGTH];
    int len = strlen(entry);
    if( !fp ) return(0);
    sprintf(t_section,"[%s]",section);    /* Format the section name */
    /*  Move through file 1 line at a time until a section is matched or EOF */
    do
    {   if( !read_line(fp,buff) )
        {   fclose(fp);
            strncpy(buffer,def,buffer_len);
            return(strlen(buffer));
        }
    }
    while( strcmp(buff,t_section) );
    /* Now that the section has been found, find the entry.
     * Stop searching upon leaving the section's area. */
    do
    {   if( !read_line(fp,buff) || buff[0] == '\0' )
        {   fclose(fp);
            strncpy(buffer,def,buffer_len);
            return(strlen(buffer));
        }
    }  while( strncmp(buff,entry,len) );
    ep = strrchr(buff,'=');    /* Parse out the equal sign */
    ep++;
    /* Copy up to buffer_len chars to buffer */
    strncpy(buffer,ep,buffer_len - 1);

    buffer[buffer_len] = '\0';
    fclose(fp);               /* Clean up and return the amount copied */
    return(strlen(buffer));
}

/*************************************************************************
 * Function:    write_private_profile_string()
 * Arguments:   <char *> section - the name of the section to search for
 *              <char *> entry - the name of the entry to find the value of
 *              <char *> buffer - pointer to the buffer that holds the string
 *              <char *> file_name - the name of the .ini file to read from
 * Returns:     TRUE if successful, otherwise FALSE
 *************************************************************************/
int WritePrivateProfileString(const char *section,
    const char *entry, const char *buffer, const char *file_name)

{   FILE *rfp, *wfp;
    char tmp_name[15];
    char buff[MAX_LINE_LENGTH];
    char t_section[MAX_LINE_LENGTH];
    int len = strlen(entry);
    mkstemp(tmp_name); /* Get a temporary file name to copy to */
    sprintf(t_section,"[%s]",section);/* Format the section name */
    if( !(rfp = fopen(file_name,"r")) )  /* If the .ini file doesn't exist */
    {   if( !(wfp = fopen(file_name,"w")) ) /*  then make one */
        {   return(0);   }
        fprintf(wfp,"%s\n",t_section);
        fprintf(wfp,"%s=%s\n",entry,buffer);
        fclose(wfp);
        return(1);
    }
    if( !(wfp = fopen(tmp_name,"w")) )
    {   fclose(rfp);
        return(0);
    }

    /* Move through the file one line at a time until a section is
     * matched or until EOF. Copy to temp file as it is read. */

    do
    {   if( !read_line(rfp,buff) )
        {   /* Failed to find section, so add one to the end */
            fprintf(wfp,"\n%s\n",t_section);
            fprintf(wfp,"%s=%s\n",entry,buffer);
            /* Clean up and rename */
            fclose(rfp);
            fclose(wfp);
            unlink(file_name);
            rename(tmp_name,file_name);
            return(1);
        }
        fprintf(wfp,"%s\n",buff);
    } while( strcmp(buff,t_section) );

    /* Now that the section has been found, find the entry. Stop searching
     * upon leaving the section's area. Copy the file as it is read
     * and create an entry if one is not found.  */
    while( 1 )
    {   if( !read_line(rfp,buff) )
        {   /* EOF without an entry so make one */
            fprintf(wfp,"%s=%s\n",entry,buffer);
            /* Clean up and rename */
            fclose(rfp);
            fclose(wfp);
            unlink(file_name);
            rename(tmp_name,file_name);
            return(1);

        }

        if( !strncmp(buff,entry,len) || buff[0] == '\0' )
            break;
        fprintf(wfp,"%s\n",buff);
    }

    if( buff[0] == '\0' )
    {   fprintf(wfp,"%s=%s\n",entry,buffer);
        do
        {
            fprintf(wfp,"%s\n",buff);
        } while( read_line(rfp,buff) );
    }
    else
    {   fprintf(wfp,"%s=%s\n",entry,buffer);
        while( read_line(rfp,buff) )
        {
             fprintf(wfp,"%s\n",buff);
        }
    }

    /* Clean up and rename */
    fclose(wfp);
    fclose(rfp);
    unlink(file_name);
    rename(tmp_name,file_name);
    return(1);
}
#endif

void GetIniString(const char *fileName, const char* section, const char* name, std::string &buf) {
	buf.resize(100);
	while (true) {
		DWORD size=GetPrivateProfileStringA(section,name,"",&buf[0],(DWORD)buf.size(),fileName);
		if (size<(buf.size()-2)) {
			buf.resize(size,true);
			return;
		}
		buf.resize(buf.size()*2);
	}	
}

IniSettings::IniSettings(int typeIdconst, const char* section, const char* name, const char *description) {
	_iniSettings.push_back(this);
	this->typeId = typeIdconst;
	this->section = section;
	this->name = name;
	this->description = description;
}

int IniSettings::GetTypeId() { return typeId; }

IniSettings::~IniSettings() {}

IniSettingsInt::IniSettingsInt(const char* section, const char* name, int defaultValue, const char *description) : IniSettings(0, section, name, description) {
	this->defaultVal=defaultValue;
}

int IniSettingsInt::GetValue(const char* fileName) {
	return GetPrivateProfileInt(section.c_str(),name.c_str(),defaultVal,fileName);
}

IniSettingsInt::~IniSettingsInt() {}

IniSettingsString::IniSettingsString(const char* section, const char* name, const char* defaultValue, const char *description) : IniSettings(1, section, name, description) {
	this->defaultVal=defaultValue;
}

void IniSettingsString::GetValue(const char* fileName, std::string &value) {
	GetIniString(fileName,section.c_str(),name.c_str(),value);
	if (value.size()==1)
		value = defaultVal;
}

IniSettingsString::~IniSettingsString() {}

IniSettingsBool::IniSettingsBool(const char* section, const char* name, bool defaultValue, const char *description) : IniSettings(2, section, name, description) {
	this->defaultVal=defaultValue;
}
bool IniSettingsBool::GetValue(const char* fileName) {
	int val = GetPrivateProfileInt(section.c_str(), name.c_str(), -100, fileName);
	if (val==-100)
		return defaultVal;
	return val!=0;
}
IniSettingsBool::~IniSettingsBool() {}

IniSettingsByteArray::IniSettingsByteArray(const char* section, const char* name, ByteArray defaultValue, const char *description) : IniSettings(3, section, name, description) {
	this->defaultVal=defaultValue;
}

void IniSettingsByteArray::GetValue(const char* fileName, ByteDynArray &value) {
	std::string buf;
	GetIniString(fileName, section.c_str(), name.c_str(), buf);
	if (buf.size()==1)
		value = defaultVal;
	else
		value.set(buf.c_str());
}

IniSettingsByteArray::~IniSettingsByteArray() {}

IniSettingsB64::IniSettingsB64(const char* section, const char* name, ByteArray defaultValue, const char *description) : IniSettings(4, section, name, description) {
	this->defaultVal=defaultValue;
}

IniSettingsB64::IniSettingsB64(const char* section, const char* name, const char *defaultValueB64, const char *description) : IniSettings(4, section, name, description) {
	CBase64 b64;
	b64.Decode(defaultValueB64,defaultVal);
}

void IniSettingsB64::GetValue(const char* fileName, ByteDynArray &value) {
	CBase64 b64;
	std::string buf;
	GetIniString(fileName, section.c_str(), name.c_str(), buf);
	if (buf.size()==1)
		value = defaultVal;
	else
		b64.Decode(buf.c_str(),value);
}

IniSettingsB64::~IniSettingsB64() {}

#ifdef _WIN64
	#pragma comment(linker, "/export:GetNumIniSettings")
	#pragma comment(linker, "/export:GetIniSettings")
#else
	#pragma comment(linker, "/export:GetNumIniSettings=_GetNumIniSettings")
	#pragma comment(linker, "/export:GetIniSettings=_GetIniSettings")
#endif

extern "C" {
	int	GetNumIniSettings() {
		return (int)_iniSettings.size();
	}

	int GetIniSettings(int i, void* data) {
		CBase64 b64;
		IniSettings* is=_iniSettings[i];
		int id=is->GetTypeId();
		std::string out;
		{
			std::stringstream th;
			th << is->section << "|" << is->name << "|" << is->description << "|" << is->GetTypeId() << "|";
			out = th.str();
		}

		std::string out2;
		if (id==0) {
			out2 = ((IniSettingsInt*)is)->defaultVal;
		}
		else if (id==1) {
			out2 = ((IniSettingsString*)is)->defaultVal;
		}
		else if (id==2) {
			out2 = ((IniSettingsBool*)is)->defaultVal ? 1 : 0;
		}
		else if (id==3 || id==4) {
			b64.Encode(((IniSettingsByteArray*)is)->defaultVal,out2);
		}
		std::string res = out + out2;
		if (data!=NULL) 
			memcpy_s(data,res.size(),res.c_str(),res.size());
		return (int)res.size();
	}
}
