[![Join the #cie-middleware channel](https://img.shields.io/badge/Slack%20channel-%23cie--middleware-blue.svg?logo=slack)](https://developersitalia.slack.com/messages/C7FPGAG94)
[![Get invited](https://slack.developers.italia.it/badge.svg)](https://slack.developers.italia.it/)
[![CIE on forum.italia.it](https://img.shields.io/badge/Forum-CIE-blue.svg)](https://forum.italia.it/c/cie) [![Build Status](https://travis-ci.com/italia/cie-middleware-linux.svg?branch=master)](https://travis-ci.com/italia/cie-middleware-linux)

Middleware della CIE (Carta di Identità Elettronica) per Windows e Linux

# MIDDLEWARE CSP-PKCS11 PER LA CIE 3.0 [![Build status](https://ci.appveyor.com/api/projects/status/dpc0ditjn04ylw6y?svg=true)](https://ci.appveyor.com/project/italia/cie-middleware)

## VERSIONE BETA

Il middleware qui presente è in fase di sviluppo, ed è da considerarsi in **versione beta**. È possibile effettuare tutti gli sviluppi e i test, ma è per ora questa base di codice **non è consigliabile per l'uso in produzione**. 

## CASO D’USO

Il middleware CIE è una libreria software che implementa le interfacce crittografiche standard **PKCS#11** e **CSP**. Esso consente agli applicativi integranti di utilizzare il certificato di autenticazione e la relativa chiave privata memorizzati sul chip della CIE astraendo dalle modalità di comunicazione di basso livello. 

## ARCHITETTURA
La libreria è sviluppata in C++ e supporta i sistemi oprativi Windows e Linux. 

# LINUX
### Prerequisiti
Fare riferimento al packet manager della propria distribuzione:
- demone pcscd. Di solito è già presente e viene lanciato come servizio automaticamente
- pcsclite-dev per la comunicazione con la smartcard
- per l'interfaccia grafica uno dei seguenti programmi eseguibili: zenity oppure kdialog (quest'ultimo con qdbus)
- openssl-dev (contenente libcrypto)
- cmake

### Build
da terminale, spostarsi nella root del progetto e digitare:
```
mkdir build && cd build
cmake .. && make
```
alla fine della build saranno presenti i file libciepki.so e gli eseguibili di test e sblocco/cambio PIN.
