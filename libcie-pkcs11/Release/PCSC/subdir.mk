################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../PCSC/APDU.cpp \
../PCSC/CardLocker.cpp \
../PCSC/PCSC.cpp \
../PCSC/Token.cpp 

OBJS += \
./PCSC/APDU.o \
./PCSC/CardLocker.o \
./PCSC/PCSC.o \
./PCSC/Token.o 

CPP_DEPS += \
./PCSC/APDU.d \
./PCSC/CardLocker.d \
./PCSC/PCSC.d \
./PCSC/Token.d 


# Each subdirectory must supply rules for building sources it contributes
PCSC/%.o: ../PCSC/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -UWIN32 -U_WIN64 -I/usr/include/PCSC -O3 -Wall -c -fmessage-length=0 -fPIC -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


