################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../PKCS11/CIEP11Template.cpp \
../PKCS11/CardContext.cpp \
../PKCS11/CardTemplate.cpp \
../PKCS11/Mechanism.cpp \
../PKCS11/P11Object.cpp \
../PKCS11/PKCS11Functions.cpp \
../PKCS11/Slot.cpp \
../PKCS11/initP11.cpp \
../PKCS11/session.cpp 

OBJS += \
./PKCS11/CIEP11Template.o \
./PKCS11/CardContext.o \
./PKCS11/CardTemplate.o \
./PKCS11/Mechanism.o \
./PKCS11/P11Object.o \
./PKCS11/PKCS11Functions.o \
./PKCS11/Slot.o \
./PKCS11/initP11.o \
./PKCS11/session.o 

CPP_DEPS += \
./PKCS11/CIEP11Template.d \
./PKCS11/CardContext.d \
./PKCS11/CardTemplate.d \
./PKCS11/Mechanism.d \
./PKCS11/P11Object.d \
./PKCS11/PKCS11Functions.d \
./PKCS11/Slot.d \
./PKCS11/initP11.d \
./PKCS11/session.d 


# Each subdirectory must supply rules for building sources it contributes
PKCS11/%.o: ../PKCS11/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -U_WIN64 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


