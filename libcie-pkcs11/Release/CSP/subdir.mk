################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../CSP/AbilitaCIE.cpp \
../CSP/ExtAuthKey.cpp \
../CSP/IAS.cpp \
../CSP/PINManager.cpp 

OBJS += \
./CSP/AbilitaCIE.o \
./CSP/ExtAuthKey.o \
./CSP/IAS.o \
./CSP/PINManager.o 

CPP_DEPS += \
./CSP/AbilitaCIE.d \
./CSP/ExtAuthKey.d \
./CSP/IAS.d \
./CSP/PINManager.d 


# Each subdirectory must supply rules for building sources it contributes
CSP/%.o: ../CSP/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -U_WIN64 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


