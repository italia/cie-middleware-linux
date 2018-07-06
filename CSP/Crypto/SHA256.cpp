#include "../StdAfx.h"
#include "sha256.h"
#include <openssl/sha.h>

static const char *szCompiledFile=__FILE__;

#ifdef WIN32

class init_sha256 {
public:
	BCRYPT_ALG_HANDLE algo;
	init_sha256() {
		if (BCryptOpenAlgorithmProvider(&algo, BCRYPT_SHA256_ALGORITHM, MS_PRIMITIVE_PROVIDER, 0) != 0)
			throw logged_error("Errore nell'inizializzazione dell'algoritmo SHA256");
	}
	~init_sha256() {
		BCryptCloseAlgorithmProvider(algo, 0);
	}
} algo_sha256;

ByteDynArray CSHA256::Digest(const ByteArray &data)
{
	BCRYPT_HASH_HANDLE hash;
	if (BCryptCreateHash(algo_sha256.algo, &hash, nullptr, 0, nullptr, 0, 0) != 0)
		throw logged_error("Errore nella creazione dell'hash SHA256");
	ByteDynArray resp(SHA256_DIGEST_LENGTH);
	if (BCryptHashData(hash, data.data(), (ULONG)data.size(), 0) != 0)
		throw logged_error("Errore nell'hash dei dati SHA256");
	if (BCryptFinishHash(hash, resp.data(), (ULONG)resp.size(), 0) != 0)
		throw logged_error("Errore nel calcolo dell'hash SHA256");
	BCryptDestroyHash(hash);

	return resp;
}

#else
	
ByteDynArray CSHA256::Digest(const ByteArray &data)
{
	ByteDynArray resp(SHA256_DIGEST_LENGTH);
	SHA256_CTX sha256;
	SHA256_Init(&sha256);
	SHA256_Update(&sha256, data.data(), data.size());
	SHA256_Final(resp.data(), &sha256);

	return resp;
}
#endif
