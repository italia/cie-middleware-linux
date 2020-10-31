################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../cie-pkcs11/Crypto/AES.cpp \
../cie-pkcs11/Crypto/ASNParser.cpp \
../cie-pkcs11/Crypto/Base64.cpp \
../cie-pkcs11/Crypto/DES3.cpp \
../cie-pkcs11/Crypto/MAC.cpp \
../cie-pkcs11/Crypto/MD5.cpp \
../cie-pkcs11/Crypto/RSA.cpp \
../cie-pkcs11/Crypto/SHA1.cpp \
../cie-pkcs11/Crypto/SHA256.cpp \
../cie-pkcs11/Crypto/SHA512.cpp 

OBJS += \
./cie-pkcs11/Crypto/AES.o \
./cie-pkcs11/Crypto/ASNParser.o \
./cie-pkcs11/Crypto/Base64.o \
./cie-pkcs11/Crypto/DES3.o \
./cie-pkcs11/Crypto/MAC.o \
./cie-pkcs11/Crypto/MD5.o \
./cie-pkcs11/Crypto/RSA.o \
./cie-pkcs11/Crypto/SHA1.o \
./cie-pkcs11/Crypto/SHA256.o \
./cie-pkcs11/Crypto/SHA512.o 

CPP_DEPS += \
./cie-pkcs11/Crypto/AES.d \
./cie-pkcs11/Crypto/ASNParser.d \
./cie-pkcs11/Crypto/Base64.d \
./cie-pkcs11/Crypto/DES3.d \
./cie-pkcs11/Crypto/MAC.d \
./cie-pkcs11/Crypto/MD5.d \
./cie-pkcs11/Crypto/RSA.d \
./cie-pkcs11/Crypto/SHA1.d \
./cie-pkcs11/Crypto/SHA256.d \
./cie-pkcs11/Crypto/SHA512.d 


# Each subdirectory must supply rules for building sources it contributes
cie-pkcs11/Crypto/%.o: ../cie-pkcs11/Crypto/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


