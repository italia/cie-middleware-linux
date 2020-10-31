################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../Crypto/AES.cpp \
../Crypto/ASNParser.cpp \
../Crypto/Base64.cpp \
../Crypto/DES3.cpp \
../Crypto/MAC.cpp \
../Crypto/MD5.cpp \
../Crypto/RSA.cpp \
../Crypto/SHA1.cpp \
../Crypto/SHA256.cpp \
../Crypto/SHA512.cpp 

OBJS += \
./Crypto/AES.o \
./Crypto/ASNParser.o \
./Crypto/Base64.o \
./Crypto/DES3.o \
./Crypto/MAC.o \
./Crypto/MD5.o \
./Crypto/RSA.o \
./Crypto/SHA1.o \
./Crypto/SHA256.o \
./Crypto/SHA512.o 

CPP_DEPS += \
./Crypto/AES.d \
./Crypto/ASNParser.d \
./Crypto/Base64.d \
./Crypto/DES3.d \
./Crypto/MAC.d \
./Crypto/MD5.d \
./Crypto/RSA.d \
./Crypto/SHA1.d \
./Crypto/SHA256.d \
./Crypto/SHA512.d 


# Each subdirectory must supply rules for building sources it contributes
Crypto/%.o: ../Crypto/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -U_WIN64 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


