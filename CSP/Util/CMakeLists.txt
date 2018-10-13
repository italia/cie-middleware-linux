target_sources(ciepki
	PRIVATE
	"${CMAKE_CURRENT_LIST_DIR}/Array.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/Array.h"
	"${CMAKE_CURRENT_LIST_DIR}/defines.h"
	"${CMAKE_CURRENT_LIST_DIR}/funccallinfo.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/funccallinfo.h"
	"${CMAKE_CURRENT_LIST_DIR}/IniSettings.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/IniSettings.h"
	"${CMAKE_CURRENT_LIST_DIR}/log.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/log.h"
	"${CMAKE_CURRENT_LIST_DIR}/ModuleInfo.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/ModuleInfo.h"
	"${CMAKE_CURRENT_LIST_DIR}/SyncroEvent.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/SyncroEvent.h"
	"${CMAKE_CURRENT_LIST_DIR}/SyncroMutex.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/SyncroMutex.h"
	"${CMAKE_CURRENT_LIST_DIR}/Thread.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/Thread.h"
	"${CMAKE_CURRENT_LIST_DIR}/TLV.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/TLV.h"
	"${CMAKE_CURRENT_LIST_DIR}/util.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/UtilException.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/UtilException.h"
	"${CMAKE_CURRENT_LIST_DIR}/util.h"
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
