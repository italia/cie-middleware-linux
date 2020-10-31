################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../cie-pkcs11/Util/Array.cpp \
../cie-pkcs11/Util/CacheLib.cpp \
../cie-pkcs11/Util/CryptoppUtils.cpp \
../cie-pkcs11/Util/IniSettings.cpp \
../cie-pkcs11/Util/ModuleInfo.cpp \
../cie-pkcs11/Util/SyncroEvent.cpp \
../cie-pkcs11/Util/SyncroMutex.cpp \
../cie-pkcs11/Util/TLV.cpp \
../cie-pkcs11/Util/UUCByteArray.cpp \
../cie-pkcs11/Util/UUCProperties.cpp \
../cie-pkcs11/Util/UUCStringTable.cpp \
../cie-pkcs11/Util/UUCTextFileReader.cpp \
../cie-pkcs11/Util/UtilException.cpp \
../cie-pkcs11/Util/funccallinfo.cpp \
../cie-pkcs11/Util/log.cpp \
../cie-pkcs11/Util/util.cpp 

OBJS += \
./cie-pkcs11/Util/Array.o \
./cie-pkcs11/Util/CacheLib.o \
./cie-pkcs11/Util/CryptoppUtils.o \
./cie-pkcs11/Util/IniSettings.o \
./cie-pkcs11/Util/ModuleInfo.o \
./cie-pkcs11/Util/SyncroEvent.o \
./cie-pkcs11/Util/SyncroMutex.o \
./cie-pkcs11/Util/TLV.o \
./cie-pkcs11/Util/UUCByteArray.o \
./cie-pkcs11/Util/UUCProperties.o \
./cie-pkcs11/Util/UUCStringTable.o \
./cie-pkcs11/Util/UUCTextFileReader.o \
./cie-pkcs11/Util/UtilException.o \
./cie-pkcs11/Util/funccallinfo.o \
./cie-pkcs11/Util/log.o \
./cie-pkcs11/Util/util.o 

CPP_DEPS += \
./cie-pkcs11/Util/Array.d \
./cie-pkcs11/Util/CacheLib.d \
./cie-pkcs11/Util/CryptoppUtils.d \
./cie-pkcs11/Util/IniSettings.d \
./cie-pkcs11/Util/ModuleInfo.d \
./cie-pkcs11/Util/SyncroEvent.d \
./cie-pkcs11/Util/SyncroMutex.d \
./cie-pkcs11/Util/TLV.d \
./cie-pkcs11/Util/UUCByteArray.d \
./cie-pkcs11/Util/UUCProperties.d \
./cie-pkcs11/Util/UUCStringTable.d \
./cie-pkcs11/Util/UUCTextFileReader.d \
./cie-pkcs11/Util/UtilException.d \
./cie-pkcs11/Util/funccallinfo.d \
./cie-pkcs11/Util/log.d \
./cie-pkcs11/Util/util.d 


# Each subdirectory must supply rules for building sources it contributes
cie-pkcs11/Util/%.o: ../cie-pkcs11/Util/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


