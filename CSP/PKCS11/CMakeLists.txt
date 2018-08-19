target_sources(ciepki
	PRIVATE
		"${CMAKE_CURRENT_LIST_DIR}/CardContext.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/CardTemplate.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/CIEP11Template.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/initP11.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/Mechanism.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/P11Object.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/PKCS11Functions.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/session.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/Slot.cpp"
		"${CMAKE_CURRENT_LIST_DIR}/CardContext.h"
		"${CMAKE_CURRENT_LIST_DIR}/CardTemplate.h"
		"${CMAKE_CURRENT_LIST_DIR}/CIEP11Template.h"
		"${CMAKE_CURRENT_LIST_DIR}/InitP11.h"
		"${CMAKE_CURRENT_LIST_DIR}/Mechanism.h"
		"${CMAKE_CURRENT_LIST_DIR}/P11Object.h"
		"${CMAKE_CURRENT_LIST_DIR}/PKCS11Functions.h"
		"${CMAKE_CURRENT_LIST_DIR}/session.h"
		"${CMAKE_CURRENT_LIST_DIR}/Slot.h"
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
