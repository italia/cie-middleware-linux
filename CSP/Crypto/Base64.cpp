#include "../StdAfx.h"
#include "Base64.h"

static const char *szCompiledFile=__FILE__;

#ifdef _WIN32
#include <Wincrypt.h>
#else

#include <openssl/bio.h>
#include <openssl/evp.h>
#include <cstring>
#include <memory>
#include <string>
#include <vector>

#include <iostream>

namespace {
	struct BIOFreeAll { void operator()(BIO* p) { BIO_free_all(p); } };
}

enum EncodingType {CRYPT_STRING_BASE64};

std::string Base64Encode(const std::vector<unsigned char>& binary)
{
	std::unique_ptr<BIO,BIOFreeAll> b64(BIO_new(BIO_f_base64()));
	BIO_set_flags(b64.get(), BIO_FLAGS_BASE64_NO_NL);
	BIO* sink = BIO_new(BIO_s_mem());
	BIO_push(b64.get(), sink);
	BIO_write(b64.get(), binary.data(), binary.size());
	BIO_flush(b64.get());
	const char* encoded;
	const long len = BIO_get_mem_data(sink, &encoded);
	return std::string(encoded, len);
}

// Assumes no newlines or extra characters in encoded string
std::vector<unsigned char> Base64Decode(const char* encoded)
{
	std::unique_ptr<BIO,BIOFreeAll> b64(BIO_new(BIO_f_base64()));
	BIO_set_flags(b64.get(), BIO_FLAGS_BASE64_NO_NL);
	BIO* source = BIO_new_mem_buf(encoded, -1); // read-only source
	BIO_push(b64.get(), source);
	const int maxlen = strlen(encoded) / 4 * 3 + 1;
	std::vector<unsigned char> decoded(maxlen);
	const int len = BIO_read(b64.get(), decoded.data(), maxlen);
	decoded.resize(len);
	return decoded;
}


void CryptBinaryToString(const char* binary, size_t binarySize, EncodingType type, char *to, size_t *toSize) 
{
	switch(type)
	{
		case CRYPT_STRING_BASE64:
		{
			std::vector<unsigned char> bin{binary, binary+binarySize}; 
			std::string enc = Base64Encode(bin);
			if(toSize)
				*toSize = enc.length();
			if(to)
				memcpy(to, enc.c_str(), enc.length());
		}
		break;
	}
}

void CryptStringToBinary(const char* encoded, size_t encodedSize, EncodingType type, char *to, size_t *toSize, void *unused1, void *unused2)
{
	switch(type)
	{
		case CRYPT_STRING_BASE64:
		{
			std::vector<unsigned char> bin = Base64Decode(encoded);
			if(toSize)
				*toSize = bin.size();
			if(to)
				memcpy(to, bin.data(), bin.size());
		}
		break;
	}
}
#endif

CBase64::CBase64()
{
}

CBase64::~CBase64()
{
}

std::string &CBase64::Encode(ByteArray &data, std::string &encodedData) {

	init_func
	DWORD dwStrSize = 0;
	CryptBinaryToString((char*)data.data(), (DWORD)data.size(), CRYPT_STRING_BASE64, NULL, &dwStrSize);
	encodedData.resize(dwStrSize);
	CryptBinaryToString((char*)data.data(), (DWORD)data.size(), CRYPT_STRING_BASE64, &encodedData.front(), &dwStrSize);

	return encodedData;
	exit_func
}

ByteDynArray &CBase64::Decode(const char *encodedData,ByteDynArray &data) {
	init_func

	DWORD dwDataSize = 0;
	CryptStringToBinary(encodedData, 0, CRYPT_STRING_BASE64, NULL, &dwDataSize, NULL, NULL);
	data.resize(dwDataSize);
	CryptStringToBinary(encodedData, 0, CRYPT_STRING_BASE64, (char*)data.data(), &dwDataSize, NULL, NULL);
	
	return data;
	exit_func
}
