target_sources(ciepki
	PRIVATE
		"${CMAKE_CURRENT_LIST_DIR}/abilitaCIE.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/abilitaCIE.h"
		"${CMAKE_CURRENT_LIST_DIR}/cambioPIN.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/cambioPIN.h"
		"${CMAKE_CURRENT_LIST_DIR}/CSP.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/ExtAuthKey.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/IAS.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/NotImpl.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/sbloccoPIN.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/sbloccoPIN.h"
		#"${CMAKE_CURRENT_LIST_DIR}/StoreProvider.cpp"
		#"${CMAKE_CURRENT_LIST_DIR}/update.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/CardMod.h"
		"${CMAKE_CURRENT_LIST_DIR}/CSP.h"
		"${CMAKE_CURRENT_LIST_DIR}/IAS.h"
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
