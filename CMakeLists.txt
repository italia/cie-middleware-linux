cmake_minimum_required(VERSION 3.5)
project(CIEPKI VERSION 1.0.0 LANGUAGES CXX)
include(GNUInstallDirs)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set( PKCS11_HEADERS PKCS11/linux.h PKCS11/pkcs11f.h PKCS11/pkcs11.h PKCS11/pkcs11t.h )
set( EXAMPLEPKCS11_SOURCES EsempioPKCS11/EsempioPKCS11.cpp )

#find PCSClite library
find_path(PCSCLITE_INCLUDE pcsclite.h WinSCard.h
	HINTS ${PC_PCSC_INCLUDEDIR} ${PC_PCSC_INCLUDE_DIRS}
	PATH_SUFFIXES PCSC)
find_library(PCSCLITE_LIBRARY NAMES PCSC WinSCard pcsclite 
	HINTS ${PC_PCSC_LIBDIR} ${PC_PCSC_LIBRARY_DIRS})

#find OpenSSL library
find_package(OpenSSL REQUIRED)
if(OPENSSL_FOUND)
	#set(OPENSSL_USE_STATIC_LIBS TRUE)
endif()
set(THREADS_PREFER_PTHREAD_FLAG ON)
find_package(Threads REQUIRED)

include_directories(PKCS11 ${OPENSSL_INCLUDE_DIR})

add_library(ciecache STATIC "")
include(CacheLib/CMakeLists.txt)

add_library(ciepki SHARED "")
target_link_libraries(ciepki ${PCSCLITE_LIBRARY} ${OPENSSL_LIBRARIES} OpenSSL::Crypto)

include(CSP/UI/CMakeLists.txt)
include(CSP/CSP/CMakeLists.txt)
include(CSP/PKCS11/CMakeLists.txt)
include(CSP/Crypto/CMakeLists.txt)
include(CSP/PCSC/CMakeLists.txt)
include(CSP/Util/CMakeLists.txt)

set_target_properties(ciecache PROPERTIES
	POSITION_INDEPENDENT_CODE ON)
set_target_properties(ciepki PROPERTIES
	VERSION ${PROJECT_VERSION}
	SOVERSION 1
	PUBLIC_HEADER PKCS11_HEADERS)
configure_file(ciepki.pc.in ciepki.pc @ONLY)
install(TARGETS ciepki 
	ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR}
	LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
	PUBLIC_HEADER DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})
install(FILES ${CMAKE_BINARY_DIR}/ciepki.pc
	DESTINATION ${CMAKE_INSTALL_DATAROOTDIR}/pkgconfig)


add_executable(EsempioPKCS11 ${EXAMPLEPKCS11_SOURCES} ${PKCS11_HEADERS})
target_link_libraries(ciepki ${CMAKE_DL_LIBS} ${OPENSSL_LIBRARIES} OpenSSL::Crypto ciecache Threads::Threads)
target_link_libraries(EsempioPKCS11 ${CMAKE_DL_LIBS} ${OPENSSL_LIBRARIES} OpenSSL::Crypto Threads::Threads)
