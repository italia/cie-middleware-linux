################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../TestCIE.cpp \
../UUCByteArray.cpp 

OBJS += \
./TestCIE.o \
./UUCByteArray.o 

CPP_DEPS += \
./TestCIE.d \
./UUCByteArray.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -pthread -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<" -ldl
	@echo 'Finished building: $<'
	@echo ' '


