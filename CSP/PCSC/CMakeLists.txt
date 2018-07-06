target_sources(ciepki
	PRIVATE
	"${CMAKE_CURRENT_LIST_DIR}/APDU.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/APDU.h"
	"${CMAKE_CURRENT_LIST_DIR}/CardLocker.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/CardLocker.h"
	"${CMAKE_CURRENT_LIST_DIR}/PCSC.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/PCSC.h"
	"${CMAKE_CURRENT_LIST_DIR}/Token.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/Token.h"
	PUBLIC
		#"${CMAKE_CURRENT_LIST_DIR}/foo.h"
	)


find_path(PCSCLITE_INCLUDE pcsclite.h WinSCard.h
	        HINTS ${PC_PCSC_INCLUDEDIR} ${PC_PCSC_INCLUDE_DIRS}
		        PATH_SUFFIXES PCSC)
#find_library(MY_LIB mylib)
#target_link_libraries(ciepki PUBLIC ${PCSCLITE_LIBRARY})
#target_compile_definitions(ciepki PUBLIC USE_MY)
target_include_directories(ciepki PRIVATE "${CMAKE_CURRENT_LIST_DIR}" "${PCSCLITE_INCLUDE}")
