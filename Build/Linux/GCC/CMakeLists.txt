cmake_minimum_required(VERSION 3.5)
project(CIEPKI VERSION 1.0.0 LANGUAGES C CXX)
include(GNUInstallDirs)

set(ROOT_DIR ${CMAKE_SOURCE_DIR}/../../../)

message ("ROOT DIR is now: ${ROOT_DIR}")

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wno-write-strings -Wno-conversion-null -Wno-pointer-arith")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wno-write-strings -Wno-conversion-null -Wno-pointer-arith")
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -D__DEBUG__ -O0")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -D__DEBUG__ -O0")

find_package(DpkgBuildFlags REQUIRED)
#dpkg-buildflags options to compile and link a Debian compliant binary
dpkg_buildflags_get_cflags(DPKG_CFLAGS)
dpkg_buildflags_get_cxxflags(DPKG_CXXFLAGS)
dpkg_buildflags_get_ldflags(DPKG_LDFLAGS)

set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s ${DPKG_CFLAGS}")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s ${DPKG_CXXFLAGS}")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${DPKG_LDFLAGS}")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} ${DPKG_LDFLAGS}")

#find PCSClite library
find_path(PCSCLITE_INCLUDE pcsclite.h WinSCard.h
	HINTS ${PC_PCSC_INCLUDEDIR} ${PC_PCSC_INCLUDE_DIRS}
	PATH_SUFFIXES PCSC)
find_library(PCSCLITE_LIBRARY NAMES PCSC WinSCard pcsclite 
	HINTS ${PC_PCSC_LIBDIR} ${PC_PCSC_LIBRARY_DIRS})

set( PKCS11_HEADERS ${ROOT_DIR}/PKCS11/linux.h ${ROOT_DIR}/PKCS11/pkcs11f.h ${ROOT_DIR}/PKCS11/pkcs11.h ${ROOT_DIR}/PKCS11/pkcs11t.h )
set( EXAMPLEPKCS11_SOURCES ${ROOT_DIR}/EsempioPKCS11/EsempioPKCS11.cpp )
set( CIEUNLOCKPIN_SOURCES ${ROOT_DIR}/Utils/sbloccopin.cpp )
set( CIECHANGEPIN_SOURCES ${ROOT_DIR}/Utils/cambiopin.cpp )
set( UTILS_HEADERS ${ROOT_DIR}/CSP/Util ${ROOT_DIR}/CSP/UI ${ROOT_DIR}/CSP/CSP ${PCSCLITE_INCLUDE} )

#find OpenSSL library
find_package(OpenSSL REQUIRED)
if(OPENSSL_FOUND)
	#set(OPENSSL_USE_STATIC_LIBS TRUE)
endif()
set(THREADS_PREFER_PTHREAD_FLAG ON)
find_package(Threads REQUIRED)

include_directories(${ROOT_DIR}PKCS11 ${OPENSSL_INCLUDE_DIR})

add_library(ciecache STATIC "")
include(${ROOT_DIR}/CacheLib/CMakeLists.txt)

add_library(ciepki SHARED "")
target_link_libraries(ciepki ${PCSCLITE_LIBRARY} ${OPENSSL_LIBRARIES} OpenSSL::Crypto)

include(${ROOT_DIR}/CSP/CMakeLists.txt)
include(${ROOT_DIR}/CSP/UI/CMakeLists.txt)
include(${ROOT_DIR}/CSP/CSP/CMakeLists.txt)
include(${ROOT_DIR}/CSP/PKCS11/CMakeLists.txt)
include(${ROOT_DIR}/CSP/Crypto/CMakeLists.txt)
include(${ROOT_DIR}/CSP/PCSC/CMakeLists.txt)
include(${ROOT_DIR}/CSP/Util/CMakeLists.txt)

set_target_properties(ciecache PROPERTIES
	POSITION_INDEPENDENT_CODE ON)
set_target_properties(ciepki PROPERTIES
	VERSION ${PROJECT_VERSION}
	SOVERSION 1
	PUBLIC_HEADER PKCS11_HEADERS)
configure_file(ciepki.pc.in ciepki.pc @ONLY)

add_executable(EsempioPKCS11 ${EXAMPLEPKCS11_SOURCES} ${PKCS11_HEADERS})
add_executable(cieunlockpin ${CIEUNLOCKPIN_SOURCES} ${UTILS_HEADERS})
add_executable(ciechangepin ${CIECHANGEPIN_SOURCES} ${UTILS_HEADERS})
target_include_directories(cieunlockpin PRIVATE ${UTILS_HEADERS})
target_include_directories(ciechangepin PRIVATE ${UTILS_HEADERS})
target_link_libraries(ciepki ${CMAKE_DL_LIBS} ${OPENSSL_LIBRARIES} OpenSSL::Crypto ciecache Threads::Threads)
target_link_libraries(EsempioPKCS11 ${CMAKE_DL_LIBS} ${OPENSSL_LIBRARIES} OpenSSL::Crypto Threads::Threads)
target_link_libraries(cieunlockpin ciepki)
target_link_libraries(ciechangepin ciepki)

install(TARGETS ciepki cieunlockpin ciechangepin
	ARCHIVE DESTINATION usr/lib
	LIBRARY DESTINATION usr/lib
	RUNTIME DESTINATION usr/bin)
#install(FILES ${CMAKE_BINARY_DIR}/ciepki.pc
#	DESTINATION usr/share/pkgconfig)


