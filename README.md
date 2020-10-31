[![Join the #cie-middleware channel](https://img.shields.io/badge/Slack%20channel-%23cie--middleware-blue.svg?logo=slack)](https://developersitalia.slack.com/messages/C7FPGAG94)
[![Get invited](https://slack.developers.italia.it/badge.svg)](https://slack.developers.italia.it/)
[![CIE on forum.italia.it](https://img.shields.io/badge/Forum-CIE-blue.svg)](https://forum.italia.it/c/cie)
[![Build Status](https://travis-ci.com/M0Rf30/cie-middleware-linux.svg?branch=master)](https://travis-ci.com/M0Rf30/cie-middleware-linux)

Middleware della CIE (Carta di Identità Elettronica) per Windows e Linux

# MIDDLEWARE CSP-PKCS11 PER LA CIE 3.0

Il middleware qui presente è in fase di sviluppo, ed è da considerarsi in **versione beta**. È possibile effettuare tutti gli sviluppi e i test, ma è per ora questa base di codice **non è consigliabile per l'uso in produzione**. 

## CASO D’USO

Il middleware CIE è una libreria software che implementa le interfacce crittografiche standard **PKCS#11** e **CSP**. Esso consente agli applicativi integranti di utilizzare il certificato di autenticazione e la relativa chiave privata memorizzati sul chip della CIE astraendo dalle modalità di comunicazione di basso livello. 

## ARCHITETTURA
La libreria è sviluppata in C++ e supporta i sistemi operativi Windows e Linux. 

# WINDOWS
La libreria è sviluppata in C++ su Visual Studio 15 2017 Community; per compilare il modulo di installazione (progetto **Setup**) è inoltre necessario [NSIS 3.02.1](https://sourceforge.net/projects/nsis/). Allo stato attuale è utilizzabile esclusivamente in ambiente Windows. Entrambe le interfacce sono esposte della stessa libreria (CIEPKI.dll), che viene compilata dal progetto CSP. La libreria viene compilata sia in versione a 32 bit che a 64 bit.
L’interfaccia CSP è conforme alla versione 7 delle specifiche dei Minidriver pubblicate da Microsoft a [questo](http://download.microsoft.com/download/7/E/7/7E7662CF-CBEA-470B-A97E-CE7CE0D98DC2/sc-minidriver_specs_V7.docx) indirizzo.
L’interfaccia PKCS11 è conforme alla specifica [RSA 2.11](https://www.cryptsoft.com/pkcs11doc/v211/).

## CSP
Il Minidriver CIE gestisce la carta in modalità **Read-Only**, come previsto dalle specifiche §7.4, pertanto i comandi di creazione e cancellazione di oggetti non sono supportati. Si faccia riferimento alla specifica Microsoft per i dettagli su quali operazioni possono essere effettuate su una carta Read Only.
Il modulo CSP implementa anche uno store provider per i certificati, in modo tale da non richiedere l’operazione di propagazione dei certificati nello store di sistema.

## PKCS11
Allo stesso modo del CSP, anche il PKCS11 gestisce la carta in modalità **read-only**. Pertanto le operazioni di creazione, modifica e distruzione di qualsiasi oggetto restituiranno un errore.

## Setup
Il modulo di installazione del Middleware si compila tramite il progetto **Setup**, che richiede l'installazione di [NSIS 3.02.1](https://sourceforge.net/projects/nsis/). Il setup installa sia la versione a 32 che a 64 bit, ed effettua la registrazione del CSP e dello Store provider. Il modulo PKCS11 non richiede registrazione, ma il nome del modulo (**CIEPKI.dll**) deve essere noto alle applicazioni che lo utilizzano.


# LINUX
### Prerequisiti
Fare riferimento al package manager della propria distribuzione:
- demone pcscd. Di solito è già presente e viene lanciato come servizio automaticamente
- pcsclite-dev per la comunicazione con la smartcard
- per l'interfaccia grafica uno dei seguenti programmi eseguibili: zenity oppure kdialog (quest'ultimo con qdbus)
- openssl-dev (contenente libcrypto)
- cmake

### Build
Da terminale, spostarsi nella root del presente repo e digitare:
```
gradle -b cie-java/build.gradle standalone
cd libcie-pkcs11/Release && make

```
alla fine della build saranno presenti i file:
* libcie-pkcs11/Release/libcie-pkcs11.so
* cie-java/bin/libs/CIEID-standalone.jar (gli eseguibili di test e sblocco/cambio PIN)
