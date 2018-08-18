#include "../StdAfx.h"
#include "sha512.h"
#ifndef _WIN32
#include <openssl/sha.h>
#endif

static const char *szCompiledFile=__FILE__;

#ifdef _WIN32

class init_sha512 {
public:
	BCRYPT_ALG_HANDLE algo;
	init_sha512() {
		if (BCryptOpenAlgorithmProvider(&algo, BCRYPT_SHA512_ALGORITHM, MS_PRIMITIVE_PROVIDER, 0) != 0)
			throw logged_error("Errore nell'inizializzazione dell'algoritmo SHA512");
	}
	~init_sha512() {
		BCryptCloseAlgorithmProvider(algo, 0);
	}
} algo_sha512;

ByteDynArray CSHA512::Digest(ByteArray &data)
{
	BCRYPT_HASH_HANDLE hash;
	if (BCryptCreateHash(algo_sha512.algo, &hash, nullptr, 0, nullptr, 0, 0) != 0)
		throw logged_error("Errore nella creazione dell'hash SHA512");
	ByteDynArray resp(SHA512_DIGEST_LENGTH);
	if (BCryptHashData(hash, data.data(), (ULONG)data.size(), 0) != 0)
		throw logged_error("Errore nell'hash dei dati SHA512");
	if (BCryptFinishHash(hash, resp.data(), (ULONG)resp.size(), 0) != 0)
		throw logged_error("Errore nel calcolo dell'hash SHA512");
	BCryptDestroyHash(hash);

	return resp;
}

#else
	
ByteDynArray CSHA512::Digest(ByteArray &data)
{
	ByteDynArray resp(SHA512_DIGEST_LENGTH);
	SHA512_CTX sha512;
	SHA512_Init(&sha512);
	SHA512_Update(&sha512, data.data(), data.size());
	SHA512_Final(resp.data(), &sha512);

	return resp;
}
#endif
