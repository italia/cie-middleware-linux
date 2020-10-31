################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../cie-pkcs11/PCSC/APDU.cpp \
../cie-pkcs11/PCSC/CardLocker.cpp \
../cie-pkcs11/PCSC/PCSC.cpp \
../cie-pkcs11/PCSC/Token.cpp 

OBJS += \
./cie-pkcs11/PCSC/APDU.o \
./cie-pkcs11/PCSC/CardLocker.o \
./cie-pkcs11/PCSC/PCSC.o \
./cie-pkcs11/PCSC/Token.o 

CPP_DEPS += \
./cie-pkcs11/PCSC/APDU.d \
./cie-pkcs11/PCSC/CardLocker.d \
./cie-pkcs11/PCSC/PCSC.d \
./cie-pkcs11/PCSC/Token.d 


# Each subdirectory must supply rules for building sources it contributes
cie-pkcs11/PCSC/%.o: ../cie-pkcs11/PCSC/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


