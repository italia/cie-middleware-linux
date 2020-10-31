################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../Util/Array.cpp \
../Util/CacheLib.cpp \
../Util/CryptoppUtils.cpp \
../Util/IniSettings.cpp \
../Util/ModuleInfo.cpp \
../Util/SyncroEvent.cpp \
../Util/SyncroMutex.cpp \
../Util/TLV.cpp \
../Util/UUCByteArray.cpp \
../Util/UUCProperties.cpp \
../Util/UUCStringTable.cpp \
../Util/UUCTextFileReader.cpp \
../Util/UtilException.cpp \
../Util/funccallinfo.cpp \
../Util/log.cpp \
../Util/util.cpp 

OBJS += \
./Util/Array.o \
./Util/CacheLib.o \
./Util/CryptoppUtils.o \
./Util/IniSettings.o \
./Util/ModuleInfo.o \
./Util/SyncroEvent.o \
./Util/SyncroMutex.o \
./Util/TLV.o \
./Util/UUCByteArray.o \
./Util/UUCProperties.o \
./Util/UUCStringTable.o \
./Util/UUCTextFileReader.o \
./Util/UtilException.o \
./Util/funccallinfo.o \
./Util/log.o \
./Util/util.o 

CPP_DEPS += \
./Util/Array.d \
./Util/CacheLib.d \
./Util/CryptoppUtils.d \
./Util/IniSettings.d \
./Util/ModuleInfo.d \
./Util/SyncroEvent.d \
./Util/SyncroMutex.d \
./Util/TLV.d \
./Util/UUCByteArray.d \
./Util/UUCProperties.d \
./Util/UUCStringTable.d \
./Util/UUCTextFileReader.d \
./Util/UtilException.d \
./Util/funccallinfo.d \
./Util/log.d \
./Util/util.d 


# Each subdirectory must supply rules for building sources it contributes
Util/%.o: ../Util/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -U_WIN64 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


