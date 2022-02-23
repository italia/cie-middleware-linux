[![Join the #cie-middleware channel](https://img.shields.io/badge/Slack%20channel-%23cie--middleware-blue.svg?logo=slack)](https://developersitalia.slack.com/messages/C7FPGAG94)
[![Get invited](https://slack.developers.italia.it/badge.svg)](https://slack.developers.italia.it/)
[![CIE on forum.italia.it](https://img.shields.io/badge/Forum-CIE-blue.svg)](https://forum.italia.it/c/cie) [![Build Status](https://travis-ci.com/italia/cie-middleware-linux.svg?branch=master)](https://travis-ci.com/italia/cie-middleware-linux)

CIE (Carta di Identità Elettronica) Linux middleware

#  CIE 3.0 PKCS11 MIDDLEWARE [![Build status](https://ci.appveyor.com/api/projects/status/dpc0ditjn04ylw6y?svg=true)](https://ci.appveyor.com/project/italia/cie-middleware)

## Disclaimer

This product is **beta software**. Use it in production at your own judgment.

## Requirements

- running pcscd
- cmake >=3.15

- pcsclite library (for SC communication )
- ssl library

On Debian and derivatives the lib requirements can be installed with the
packages `libpcsclite-dev libssl-dev`.
Library versions as of Ubuntu 18.04 are reported to work.


The official building approach is using Eclipse, for historical reasons.
Versions from 4.18 onward are working, this is due to JDT version being tied
to the IDE's one.
For a more up-to-date approach using gradle check the user fork mentioned in
the comments at [1].


[1]: https://github.com/italia/cie-middleware-linux/issues/6


## Build

Build happens in three steps:

1. build signing library `cie_sign_sdk` using cmake
1. build C++ middleware project `cie-pkcs11` using Eclipse
1. compile Java application `CIEID` using Eclipse


### cie_sign_sdk

This will build a static library and copy it into `cie-pkcs11/Sign`

    cd cie_sign_sdk
    cmake -B build/
    cmake --build build/
    cmake --install build/

### cie-pkcs11

Open the repository root directory with Eclipse, its auto-discovery tool should
find at least two projects:

- cie-pkcs11, a C++ project
- CIEID, a Java project

In *Project Explorer* view, select the project root, then select menu item
`Project > Build project`.
This should leave a `libcie-pkcs11.so` object in Debug (the default target).

### CIEID

These steps can be performed with or without Eclipse.

If using Eclipse install the *JDT* plugin, switch to "Java" *perspective*,
select CIEID in the *Package Explorer* view, add a Debug or Run configuration
starting `it.ipzs.cieid.MainApplication` as main class.

Add `-Djna.library.path=".:../Debug"` to VM arguments.

When directly calling the JVM be sure to make the `libcie-pkcs11.so` available
to JNA either using the `jna.library.path` property or installing the library
in a path searched by default, e.g. `/usr/local/lib`.
