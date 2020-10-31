################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../cie-pkcs11/PKCS11/CIEP11Template.cpp \
../cie-pkcs11/PKCS11/CardContext.cpp \
../cie-pkcs11/PKCS11/CardTemplate.cpp \
../cie-pkcs11/PKCS11/Mechanism.cpp \
../cie-pkcs11/PKCS11/P11Object.cpp \
../cie-pkcs11/PKCS11/PKCS11Functions.cpp \
../cie-pkcs11/PKCS11/Slot.cpp \
../cie-pkcs11/PKCS11/initP11.cpp \
../cie-pkcs11/PKCS11/session.cpp 

OBJS += \
./cie-pkcs11/PKCS11/CIEP11Template.o \
./cie-pkcs11/PKCS11/CardContext.o \
./cie-pkcs11/PKCS11/CardTemplate.o \
./cie-pkcs11/PKCS11/Mechanism.o \
./cie-pkcs11/PKCS11/P11Object.o \
./cie-pkcs11/PKCS11/PKCS11Functions.o \
./cie-pkcs11/PKCS11/Slot.o \
./cie-pkcs11/PKCS11/initP11.o \
./cie-pkcs11/PKCS11/session.o 

CPP_DEPS += \
./cie-pkcs11/PKCS11/CIEP11Template.d \
./cie-pkcs11/PKCS11/CardContext.d \
./cie-pkcs11/PKCS11/CardTemplate.d \
./cie-pkcs11/PKCS11/Mechanism.d \
./cie-pkcs11/PKCS11/P11Object.d \
./cie-pkcs11/PKCS11/PKCS11Functions.d \
./cie-pkcs11/PKCS11/Slot.d \
./cie-pkcs11/PKCS11/initP11.d \
./cie-pkcs11/PKCS11/session.d 


# Each subdirectory must supply rules for building sources it contributes
cie-pkcs11/PKCS11/%.o: ../cie-pkcs11/PKCS11/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


