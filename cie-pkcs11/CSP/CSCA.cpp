#include "CSCA.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <regex>
#include <curl/curl.h>
#include <sys/stat.h>
#include <unistd.h>
#include <openssl/pem.h>
#include <openssl/err.h>
#include "../LOGGER/Logger.h"

using namespace CieIDLogger;

// Definizione delle URL dei certificati CSCA
const std::vector<std::string> CSCA::CSCA_URLS = {
	"https://www.idea.ipzs.it/downloadCer.html?nomeFile=436CE3921D10922307EFD7A2F577ED7524467F1B.cer",
	"https://www.idea.ipzs.it/downloadCer.html?nomeFile=852DF7A70A512D83103DFBC9F628CB6B1CEE5591.cer",
	"https://www.idea.ipzs.it/downloadCer.html?nomeFile=A0F56552180CCBCC0FFD7D0DF39F8604C7C98F62.cer",
	"https://www.idea.ipzs.it/downloadCer.html?nomeFile=B0BF3BB9ECEBC720974C1D13A5905A1A613589A0.cer",
	"https://www.idea.ipzs.it/downloadCer.html?nomeFile=D11A505E15ADEA5A61779CA4A2A991EC3949D1F9.cer",
	"https://www.idea.ipzs.it/downloadCer.html?nomeFile=E94A91197072CD256951790E6CFE2386EDB09D6E.cer"
};

// Callback per scrivere dati scaricati da cURL
static size_t WriteCallback(void* contents, size_t size, size_t nmemb, void* userp) {
	FILE* fp = static_cast<FILE*>(userp);
	return fwrite(contents, size, nmemb, fp);
}

CSCA::CSCA() : trustStore_(nullptr), loggingEnabled_(true), initialized_(false) {
	// Inizializza libcurl globalmente
	curl_global_init(CURL_GLOBAL_DEFAULT);

	// Crea il trust store
	trustStore_ = X509_STORE_new();
	if (!trustStore_) {
		throw std::runtime_error("Errore nella creazione del trust store");
	}
}

CSCA::~CSCA() {
	cleanup();
	
	// Cleanup libcurl
	curl_global_cleanup();
	

}

std::string CSCA::getCSCARootDir() {
	// Usa lo stesso percorso sandbox utilizzato per log e cache
	// Tipicamente in: ~/Library/Containers/it.ipzs.cie.cieid/Data/
	const char* home = getenv("HOME");
	if (home) {
		std::string path = std::string(home) + "/Library/Containers/it.ipzs.cie.cieid/Data/CIEPKI";
		
		// Crea la directory se non esiste
		struct stat st = {0};
		if (stat(path.c_str(), &st) == -1) {
			mkdir(path.c_str(), 0700);
		}
		
		return path;
	}
	
	// Fallback: directory temporanea
	return "/tmp/cie_csca";
}

bool CSCA::initialize() {
	logMessage("Inizializzazione CSCA - Download certificati");

	bool success = false;
	std::vector<std::string> tempFiles;
	std::string rootDir = getCSCARootDir();
	
	// Crea la directory CSCA se non esiste
	struct stat st = {0};
	if (stat(rootDir.c_str(), &st) == -1) {
		mkdir(rootDir.c_str(), 0700);
	}

	for (size_t i = 0; i < CSCA_URLS.size(); ++i) {
		// Estrae l'hash dal nome del file nell'URL
		std::string hashName = extractHashFromUrl(CSCA_URLS[i]);
		std::string tempFile = generateTempFilename(hashName);
		std::string targetPath = rootDir + "/" + hashName + ".cer";
		
		// Verifica se il certificato esiste già in cache
		if (access(targetPath.c_str(), F_OK) == 0) {
			logMessage("Certificato CSCA trovato in cache: " + hashName + ".cer");
			X509* cert = loadCertificateFromFile(targetPath);
			if (cert) {
				cscaCertificates_.push_back(cert);
				X509_STORE_add_cert(trustStore_, cert);
				success = true;
				continue;
			}
		}

		// Download del certificato
		if (downloadCertificate(CSCA_URLS[i], tempFile)) {
			X509* cert = loadCertificateFromFile(tempFile);

			if (cert) {
				cscaCertificates_.push_back(cert);
				X509_STORE_add_cert(trustStore_, cert);
				logMessage("Certificato CSCA caricato: " + hashName + ".cer");
				
				// Salva in cache
				rename(tempFile.c_str(), targetPath.c_str());
				success = true;
			}
			else {
				logMessage("Errore nel caricamento del certificato: " + hashName + ".cer");
				unlink(tempFile.c_str());
			}
		}
		else {
			logMessage("Errore nel download del certificato: " + hashName + ".cer");
		}
	}

	if (!success) {
		logMessage("Errore: Nessun certificato CSCA scaricato");
		return false;
	}

	initialized_ = true;
	logMessage("Inizializzazione CSCA completata - " + std::to_string(cscaCertificates_.size()) + " certificati caricati");
	return true;
}

bool CSCA::loadFromFiles(const std::vector<std::string>& certFiles) {
	logMessage("Caricamento certificati CSCA da file");

	bool success = false;
	for (const auto& filename : certFiles) {
		X509* cert = loadCertificateFromFile(filename);
		if (cert) {
			cscaCertificates_.push_back(cert);
			X509_STORE_add_cert(trustStore_, cert);
			logMessage("Certificato caricato: " + filename);
			success = true;
		}
		else {
			logMessage("Errore nel caricamento: " + filename);
		}
	}

	if (success) {
		initialized_ = true;
		logMessage("Caricamento certificati completato - " + std::to_string(cscaCertificates_.size()) + " certificati");
	}

	return success;
}

bool CSCA::verifyCertificate(const std::string& certPath) {
	if (!initialized_) {
		throw std::runtime_error("CSCA non inizializzato");
	}

	logMessage("Verifica catena CSCA per: " + certPath);

	X509* certDS = loadCertificateFromFile(certPath);
	if (!certDS) {
		throw std::runtime_error("Impossibile caricare il certificato Document Signer: " + certPath);
	}

	bool result = verifyInternal(certDS);
	X509_free(certDS);

	return result;
}

bool CSCA::verifyCertificate(const unsigned char* certData, size_t certSize) {
	if (!initialized_) {
		throw std::runtime_error("CSCA non inizializzato");
	}

	X509* certDS = loadCertificateFromData(certData, certSize);
	if (!certDS) {
		throw std::runtime_error("Impossibile caricare il certificato Document Signer da dati binari");
	}

	bool result = verifyInternal(certDS);
	X509_free(certDS);

	return result;
}

std::vector<X509*> CSCA::getCertificateChain(const std::string& certPath) {
	std::vector<X509*> chain;

	if (!initialized_) {
		return chain;
	}

	X509* certDS = loadCertificateFromFile(certPath);
	if (!certDS) {
		return chain;
	}

	X509_STORE_CTX* ctx = X509_STORE_CTX_new();
	if (!ctx) {
		X509_free(certDS);
		return chain;
	}

	X509_STORE_CTX_init(ctx, trustStore_, certDS, nullptr);

	if (X509_verify_cert(ctx) == 1) {
		STACK_OF(X509)* sk_chain = X509_STORE_CTX_get1_chain(ctx);
		if (sk_chain) {
			for (int i = 0; i < sk_X509_num(sk_chain); ++i) {
				X509* cert = sk_X509_value(sk_chain, i);
				X509_up_ref(cert);
				chain.push_back(cert);
			}
			sk_X509_pop_free(sk_chain, X509_free);
		}
	}

	X509_STORE_CTX_free(ctx);
	X509_free(certDS);

	return chain;
}

void CSCA::cleanup() {
	// Libera i certificati
	for (X509* cert : cscaCertificates_) {
		if (cert) {
			X509_free(cert);
		}
	}
	cscaCertificates_.clear();

	// Libera il trust store
	if (trustStore_) {
		X509_STORE_free(trustStore_);
		trustStore_ = nullptr;
	}

	initialized_ = false;
	logMessage("Cleanup CSCA completato");
}

// Metodi privati
std::string CSCA::extractHashFromUrl(const std::string& url) {
	// Cerca il pattern nomeFile=HASH.cer
	std::regex pattern(R"(nomeFile=([A-F0-9]+)\.cer)", std::regex_constants::icase);
	std::smatch match;

	if (std::regex_search(url, match, pattern)) {
		return match[1].str();
	}

	// Fallback: usa un nome generico se non trova l'hash
	logMessage("Avviso: Impossibile estrarre hash dall'URL: " + url);
	return "unknown_hash_" + std::to_string(std::hash<std::string>{}(url) % 10000);
}

std::string CSCA::generateTempFilename(const std::string& hash) {
	std::ostringstream oss;
	oss << "/tmp/csca_" << hash << "_" << getpid() << ".cer";
	return oss.str();
}

bool CSCA::downloadCertificate(const std::string& url, const std::string& filename) {
	std::string hashName = extractHashFromUrl(url);
	logMessage("Download certificato CSCA: " + hashName + ".cer");

	CURL* curl = curl_easy_init();
	if (!curl) {
		logMessage("Errore: impossibile inizializzare cURL");
		return false;
	}

	FILE* fp = fopen(filename.c_str(), "wb");
	if (!fp) {
		curl_easy_cleanup(curl);
		logMessage("Errore: impossibile aprire il file: " + filename);
		return false;
	}

	curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
	curl_easy_setopt(curl, CURLOPT_WRITEDATA, fp);
	curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
	curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 1L);
	curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 2L);
	curl_easy_setopt(curl, CURLOPT_TIMEOUT, 30L);

	CURLcode res = curl_easy_perform(curl);
	
	fclose(fp);
	curl_easy_cleanup(curl);

	if (res != CURLE_OK) {
		std::ostringstream oss;
		oss << "Errore nel download di " << hashName << ".cer - " << curl_easy_strerror(res);
		logMessage(oss.str());
		unlink(filename.c_str());
		return false;
	}

	logMessage("Download completato: " + hashName + ".cer");
	return true;
}

X509* CSCA::loadCertificateFromData(const unsigned char* data, size_t size) {
	if (!data || size == 0) {
		return nullptr;
	}

	BIO* bio = BIO_new_mem_buf(data, static_cast<int>(size));
	if (!bio) {
		return nullptr;
	}

	X509* cert = d2i_X509_bio(bio, nullptr);
	if (!cert) {
		// Prova anche il formato PEM
		BIO_reset(bio);
		cert = PEM_read_bio_X509(bio, nullptr, nullptr, nullptr);
	}

	BIO_free(bio);
	return cert;
}

X509* CSCA::loadCertificateFromFile(const std::string& filename) {
	FILE* fp = fopen(filename.c_str(), "rb");
	if (!fp) {
		return nullptr;
	}

	X509* cert = d2i_X509_fp(fp, nullptr);
	if (!cert) {
		// Prova anche il formato PEM
		fseek(fp, 0, SEEK_SET);
		cert = PEM_read_X509(fp, nullptr, nullptr, nullptr);
	}

	fclose(fp);
	return cert;
}

bool CSCA::isSelfSigned(X509* cert) {
	if (!cert) {
		return false;
	}

	X509_NAME* subject = X509_get_subject_name(cert);
	X509_NAME* issuer = X509_get_issuer_name(cert);

	return (subject && issuer && X509_NAME_cmp(subject, issuer) == 0);
}

bool CSCA::verifyInternal(X509* certDS) {
	if (!certDS) {
		return false;
	}

	// Crea il contesto di verifica
	X509_STORE_CTX* ctx = X509_STORE_CTX_new();
	if (!ctx) {
		throw std::runtime_error("Errore nella creazione del contesto di verifica");
	}

	X509_STORE_CTX_init(ctx, trustStore_, certDS, nullptr);
	int result = X509_verify_cert(ctx);

	if (result != 1) {
		int error = X509_STORE_CTX_get_error(ctx);
		std::string errorMsg = "Il certificato di Document Signer non è valido: ";
		errorMsg += X509_verify_cert_error_string(error);

		X509_STORE_CTX_free(ctx);
		throw std::runtime_error(errorMsg);
	}

	// Verifica che il root sia self-signed
	STACK_OF(X509)* chain = X509_STORE_CTX_get1_chain(ctx);
	if (chain && sk_X509_num(chain) > 0) {
		X509* rootCert = sk_X509_value(chain, sk_X509_num(chain) - 1);
		if (!isSelfSigned(rootCert)) {
			sk_X509_pop_free(chain, X509_free);
			X509_STORE_CTX_free(ctx);
			throw std::runtime_error("Impossibile validare il certificato di Document Signer - Root non self-signed");
		}
		sk_X509_pop_free(chain, X509_free);
	}

	X509_STORE_CTX_free(ctx);
	logMessage("Verifica CSCA completata con successo");
	return true;
}

void CSCA::logMessage(const std::string& msg) {
	if (loggingEnabled_) {
		LOG_INFO(msg.c_str());
	}
}
