target_sources(ciepki
	PRIVATE
	"${CMAKE_CURRENT_LIST_DIR}/AtlBitmapSkinButton.h"
	"${CMAKE_CURRENT_LIST_DIR}/atlcontrols.h"
	"${CMAKE_CURRENT_LIST_DIR}/Bitmap.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/Bitmap.h"
	"${CMAKE_CURRENT_LIST_DIR}/Message.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/Message.h"
	"${CMAKE_CURRENT_LIST_DIR}/PIN.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/PIN.h"
	"${CMAKE_CURRENT_LIST_DIR}/safeDesktop.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/safeDesktop.h"
	"${CMAKE_CURRENT_LIST_DIR}/SystemTray.h"
	"${CMAKE_CURRENT_LIST_DIR}/SystemTray.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/VCEdit.h"
	"${CMAKE_CURRENT_LIST_DIR}/Verifica.h"
	"${CMAKE_CURRENT_LIST_DIR}/Verifica.cpp"
	"${CMAKE_CURRENT_LIST_DIR}/helper"
	"${CMAKE_CURRENT_LIST_DIR}/helper.cpp"
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
