################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../cie-pkcs11/CSP/AbilitaCIE.cpp \
../cie-pkcs11/CSP/ExtAuthKey.cpp \
../cie-pkcs11/CSP/IAS.cpp \
../cie-pkcs11/CSP/PINManager.cpp 

OBJS += \
./cie-pkcs11/CSP/AbilitaCIE.o \
./cie-pkcs11/CSP/ExtAuthKey.o \
./cie-pkcs11/CSP/IAS.o \
./cie-pkcs11/CSP/PINManager.o 

CPP_DEPS += \
./cie-pkcs11/CSP/AbilitaCIE.d \
./cie-pkcs11/CSP/ExtAuthKey.d \
./cie-pkcs11/CSP/IAS.d \
./cie-pkcs11/CSP/PINManager.d 


# Each subdirectory must supply rules for building sources it contributes
cie-pkcs11/CSP/%.o: ../cie-pkcs11/CSP/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


