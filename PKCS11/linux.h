

#ifndef LINUX_H
#define LINUX_H

#define CK_PTR *

#define CK_DEFINE_FUNCTION(returnType, name) \
   __attribute__ ((visibility ("default"))) returnType name

#define CK_DECLARE_FUNCTION(returnType, name) \
   __attribute__ ((visibility ("default"))) returnType name

#define CK_DECLARE_FUNCTION_POINTER(returnType, name) \
   /*__attribute__ ((visibility ("default")))*/ returnType (* name)

#define CK_CALLBACK_FUNCTION(returnType, name) \
   returnType (* name)

#ifndef NULL_PTR
#define NULL_PTR 0
#endif

#endif
