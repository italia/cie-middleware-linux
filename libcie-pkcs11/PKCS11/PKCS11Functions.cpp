// P11Emissione.cpp : Defines the entry point for the DLL application.
//
#include "wintypes.h"
#include "PKCS11Functions.h"
#include "InitP11.h"
#ifdef WIN32
#include <winscard.h>
#else
#include <PCSC/winscard.h>
#endif
#include "session.h"
#include "CardTemplate.h"
#ifdef WIN32
#include <malloc.h>
#else
#include <stdlib.h>
#endif
#include "../Util/ModuleInfo.h"
#include "../Util/util.h"
#include "../Util/SyncroEvent.h"
#include <mutex>

#include <cryptopp/misc.h>
#include "../Util/UUCByteArray.h"

CLog Log;

// flag: P11 inizializzato
bool bP11Initialized=false;
bool bP11Terminate=false;

using namespace p11;

std::mutex p11Mutex;
auto_reset_event p11slotEvent/*("CardOS_P11_Event")*/;

// meccanismi supportati


CK_MECHANISM_TYPE P11mechanisms[]= {
    //CKM_RSA_PKCS_KEY_PAIR_GEN,
    CKM_RSA_PKCS,
    CKM_MD5,
    CKM_SHA_1,
    CKM_SHA256,
    CKM_SHA1_RSA_PKCS,
    CKM_SHA256_RSA_PKCS,
    CKM_MD5_RSA_PKCS
};


const char *getAttributeName(unsigned long dwId);
//extern CModuleInfo moduleInfo; // informazioni sulla dll (o so)
bool bModuleInit=false;

#ifdef WIN32
// funzione DllMain
// inizilizzo moduleInfo
BOOL APIENTRY DllMainP11( HANDLE hModule,
                          DWORD  ul_reason_for_call,
                          LPVOID lpReserved
                        )
{
    if (ul_reason_for_call==DLL_PROCESS_ATTACH && !bModuleInit) {
        bModuleInit=true;
        moduleInfo.init(hModule);
        std::string mainMutexName;
        //mainMutexName="CIE_P11_Mutex_"+moduleInfo.szModuleName;
        //p11Mutex.Create(mainMutexName.c_str());
        //xmlInit();
        std::string configPath;
        configPath = moduleInfo.szModulePath + moduleInfo.szModuleName + ".ini";
        initLog(configPath.c_str(), __DATE__ " " __TIME__);
        Log.initModule("PKCS11", __DATE__ " " __TIME__);
        p11::InitP11(configPath.c_str());

    }

    if (ul_reason_for_call==DLL_PROCESS_DETACH && bModuleInit) {

        if (bP11Initialized) {
            Log.write("%s","Forzatura C_Finalize");
            C_Finalize(NULL);
            bP11Initialized = false;
        }
        //xmlCleanup();
        bModuleInit=false;
        bP11Terminate=true;

        CSlot::DeleteSlotList();
        CCardTemplate::DeleteTemplateList();
        p11slotEvent.set();

    }

    return TRUE;
}

#else

/* ---- GENERATE CK_FUNCTION_LIST */

#define CK_PKCS11_FUNCTION_INFO(name) \
name,
//
//CK_FUNCTION_LIST pkcs11_function_list = {
//    { LIBRARY_VERSION_MAJOR, LIBRARY_VERSION_MINOR },
//#include "pkcs11f.h"
//};
#undef CK_PKCS11_FUNCTION_INFO


__attribute__((constructor)) void DllMainAttach()
{
    // code
    bModuleInit=true;
    std::string configPath = "/usr/lib/ciepki.ini";
    initLog("CIEPKC11", configPath.c_str(), __DATE__ " " __TIME__);
    p11::InitP11(configPath.c_str());
}

__attribute__((destructor)) void DllMainDetach()
{
    Log.write("%s","DllMainDetach");

    if (bP11Initialized) {
        Log.write("%s","Forzatura C_Finalize");
        C_Finalize(NULL);
        bP11Initialized = false;

        CSlot::DeleteSlotList();
        CCardTemplate::DeleteTemplateList();
        try {
            p11slotEvent.set();
        } catch(...) {
            printf("event set error");
        }
    }

    //xmlCleanup();
    bModuleInit=false;
    bP11Terminate=true;
}

#endif

void WriteAttributes(CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount)
{
    Log.write("Attributes: %x", ulCount);
    for(unsigned int i = 0; i < ulCount; i++) {
        if(pTemplate[i].pValue) {
            switch(pTemplate[i].type) {
            case CKA_CLASS:
                Log.writePure("%d) type=%x (%s), value=%x, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), *(CK_OBJECT_CLASS_PTR)(pTemplate[i].pValue), pTemplate[i].ulValueLen);
                break;

            case CKA_TOKEN:
            case CKA_PRIVATE:
            case CKA_MODIFIABLE:
                Log.writePure("%d) type=%x (%s), value=%x, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), *(CK_BBOOL*)(pTemplate[i].pValue), pTemplate[i].ulValueLen);
                break;

            case CKA_LABEL:
            case CKA_OBJECT_ID:
                Log.writePure("%d) type=%x (%s), value=%s, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), (char*)(pTemplate[i].pValue), pTemplate[i].ulValueLen);
                break;

            case CKA_VALUE:
                Log.writePure("%d) type=%x (%s), value=%s, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), UUCByteArray((BYTE*)pTemplate[i].pValue, pTemplate[i].ulValueLen).toHexString(), pTemplate[i].ulValueLen);
                break;

            default:
                Log.writePure("%d) type=%x (%s), value=%p, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), pTemplate[i].pValue, pTemplate[i].ulValueLen);
                break;
            }
        } else {
            switch(pTemplate[i].type) {
            case CKA_CLASS:
                Log.writePure("%d) type=%x (%s), value=NULL, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), pTemplate[i].ulValueLen);
                break;

            case CKA_TOKEN:
            case CKA_PRIVATE:
            case CKA_MODIFIABLE:
                Log.writePure("%d) type=%x (%s), value=NULL, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), pTemplate[i].ulValueLen);
                break;

            case CKA_LABEL:
            case CKA_OBJECT_ID:
                Log.writePure("%d) type=%x (%s), value=NULL, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), pTemplate[i].ulValueLen);
                break;

            default:
                Log.writePure("%d) type=%x (%s), value=NULL, len=%x", i + 1, pTemplate[i].type, getAttributeName(pTemplate[i].type), pTemplate[i].ulValueLen);
                break;
            }
        }
    }
}

void WriteMechanism(CK_MECHANISM_PTR pMechanism)
{
    Log.write("Mechanism : %x", pMechanism->mechanism);
    Log.write("Parameter: %x", pMechanism->pParameter);
    Log.write("Parameter len: %x", pMechanism->ulParameterLen);
}

// funzione CheckMechanismParam
bool CheckMechanismParam(CK_MECHANISM *pParam)
{
    init_func
    // tutti i mechanism non hanno parametri
    if (pParam->pParameter==NULL && pParam->ulParameterLen==0) {
        return true;
    }
    return false;
}

CK_RV CK_ENTRY C_UpdateSlotList()
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    CSlot::InitSlotList();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_AbilitaCIE(const char*  szPAN, const char*  szPIN)//, PROGRESS_CALLBACK progressCallBack)
{
    return 0;
}

CK_RV CK_ENTRY C_GetSlotList(CK_BBOOL tokenPresent, CK_SLOT_ID_PTR pSlotList, CK_ULONG_PTR pulCount)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutArray(pSlotList, pulCount)

    logParam(tokenPresent)
    logParam(pSlotList)
    logParam(pulCount)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    bool bOver=false;
    std::vector<CK_SLOT_ID> slotsRet;
    unsigned int iCnt;
    if (tokenPresent) {
        // solo i lettori con la carta inserita
        iCnt=0;

        if (CSlot::g_mSlots.size()==0) {
            *pulCount=0;
            return CKR_OK;

        }
        SlotMap::const_iterator it=CSlot::g_mSlots.end();
        do {
            it--;
            std::shared_ptr<CSlot> pSlot=it->second;

            if (pSlot->IsTokenPresent()) {
                if (pSlotList) {
                    if (iCnt<*pulCount)
                        pSlotList[iCnt]=pSlot->hSlot;
                    else
                        bOver=true;
                }
                iCnt++;
            }
        } while (it!=CSlot::g_mSlots.begin());
    } else {
        // restituisco tutti i lettori
        if (!pSlotList) {
            iCnt=(CK_ULONG)CSlot::g_mSlots.size();
        } else {
            iCnt=0;
            SlotMap::const_iterator it=CSlot::g_mSlots.end();
            while (it!=CSlot::g_mSlots.begin()) {
                it--;
                if (iCnt<*pulCount)
                    pSlotList[iCnt]=it->first;
                else
                    bOver=true;
                iCnt++;
            }
        }
    }
    *pulCount=iCnt;
    if (bOver)
        throw p11_error(CKR_BUFFER_TOO_SMALL);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Initialize(CK_VOID_PTR pReserved)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(pReserved)

//    CK_C_INITIALIZE_ARGS_PTR ptr=(CK_C_INITIALIZE_ARGS_PTR)pReserved;

    if (bP11Initialized)
        return CKR_OK;
    //	throw p11_error(CKR_CRYPTOKI_ALREADY_INITIALIZED)

    // verifico che i flag siano supportati
    CK_C_INITIALIZE_ARGS_PTR iargs = NULL_PTR;
    if (pReserved) {
        iargs =	(CK_C_INITIALIZE_ARGS_PTR)pReserved;
        if (iargs->pReserved != NULL)
            throw p11_error(CKR_ARGUMENTS_BAD);

        if (iargs->flags	& CKF_OS_LOCKING_OK) {
            if ((iargs->CreateMutex) || (iargs->DestroyMutex) || (iargs->LockMutex) || (iargs->UnlockMutex))
                throw p11_error(CKR_CANT_LOCK);
        } else if (iargs->flags == 0) {
            if ((iargs->CreateMutex) || (iargs->DestroyMutex) || (iargs->LockMutex) || (iargs->UnlockMutex))
                throw p11_error(CKR_CANT_LOCK);
        } else if (iargs->flags & CKF_LIBRARY_CANT_CREATE_OS_THREADS)
            throw p11_error(CKR_NEED_TO_CREATE_THREADS);
        else
            throw p11_error(CKR_ARGUMENTS_BAD);
    }


    if (CCardTemplate::g_mCardTemplates.size()==0)
        CCardTemplate::InitTemplateList();

    bP11Initialized = true;

    CSlot::InitSlotList();

    Log.write("C_Initialize ok");

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Finalize(CK_VOID_PTR pReserved)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(pReserved)

    if (pReserved != NULL)
        throw p11_error(CKR_ARGUMENTS_BAD);

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    bP11Initialized = false;

    // TODO verificare thread "vuoto"
    if (CSlot::Thread.joinable()) {
        CCardContext *tc = CSlot::ThreadContext;
        if (tc != nullptr) {
            SCARDCONTEXT hC = tc->hContext;
            if (hC)
                SCardCancel(hC);
        }
        p11Mutex.unlock();
        CSlot::Thread.join();
        p11Mutex.lock();
    }


    for(SlotMap::const_iterator it=CSlot::g_mSlots.begin(); it!=CSlot::g_mSlots.end(); it++) {
        it->second->CloseAllSessions();
    }

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_OpenSession(CK_SLOT_ID slotID, CK_FLAGS flags, CK_VOID_PTR pApplication, CK_NOTIFY notify, CK_SESSION_HANDLE_PTR phSession)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(phSession)

    logParam(slotID)
    logParam(flags)
    logParam(pApplication)
    logParam(notify)
    logParam(phSession)




    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    if (!(flags & CKF_SERIAL_SESSION))
        throw p11_error(CKR_SESSION_PARALLEL_NOT_SUPPORTED);

    std::shared_ptr<CSlot> pSlot = CSlot::GetSlotFromID(slotID);
    if (pSlot == nullptr)
        throw p11_error(CKR_SLOT_ID_INVALID);

    auto pSession = std::unique_ptr<CSession>(new CSession());
    pSession->pSlot=pSlot;
    pSession->flags=flags;
    pSession->notify=notify;
    pSession->pApplication=pApplication;

    if ((flags & CKF_RW_SESSION)==0) {
        // se una sessione RO devo
        // verificare che non ci siano sessioni SO RW
        if (pSession->ExistsSO_RW())
            throw p11_error(CKR_SESSION_READ_WRITE_SO_EXISTS);
    }

    pSlot->Init();

    pSession->slotID=slotID;

// aggiungo la sessione all'elenco globale
    *phSession = CSession::AddSession(std::move(pSession));

    if (Log.Enabled) {
        Log.writePure("Sessione: %i",*phSession);
        Log.writePure("Lettore: %s",pSlot->szName.c_str());
        Log.writePure("CardManager: %s",pSlot->pTemplate->szName.c_str());
        std::string szModel;
        pSlot->pTemplate->FunctionList.templateGetModel(*pSlot,szModel);
        Log.writePure("Tipo Carta: %s",szModel.c_str());
    }

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetTokenInfo(CK_SLOT_ID slotID, CK_TOKEN_INFO_PTR pInfo)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(pInfo)

    logParam(slotID)
    logParam(pInfo)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSlot> pSlot = CSlot::GetSlotFromID(slotID);

    if (pSlot == nullptr)
        throw p11_error(CKR_SLOT_ID_INVALID);

    if (pInfo == nullptr)
        throw p11_error(CKR_ARGUMENTS_BAD);

    if (!pSlot->IsTokenPresent())
        throw p11_error(CKR_TOKEN_NOT_PRESENT);

    pSlot->GetTokenInfo(pInfo);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_CloseSession(CK_SESSION_HANDLE hSession)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(hSession)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    CSession::DeleteSession(hSession);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_CloseAllSessions(CK_SLOT_ID slotID)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(slotID)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSlot> pSlot = CSlot::GetSlotFromID(slotID);

    if (pSlot==nullptr)
        throw p11_error(CKR_SLOT_ID_INVALID);

    pSlot->CloseAllSessions();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}


CK_RV CK_ENTRY C_GetSlotInfo(CK_SLOT_ID slotID, CK_SLOT_INFO_PTR pInfo)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(pInfo)

    logParam(slotID)
    logParam(pInfo)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSlot> pSlot = CSlot::GetSlotFromID(slotID);
    if (pSlot==NULL)
        throw p11_error(CKR_SLOT_ID_INVALID);

    if (pInfo == NULL)
        throw p11_error(CKR_ARGUMENTS_BAD);

    pSlot->GetInfo(pInfo);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetInfo(CK_INFO_PTR pInfo /* location that receives information */)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(pInfo)

    logParam(pInfo)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    pInfo->cryptokiVersion.major = 2; /* Cryptoki interface ver */
    pInfo->cryptokiVersion.minor = 10;   //12345678901234567890123456789012
    CryptoPP::memcpy_s((char*)pInfo->manufacturerID,32,"IPZS\0                           ", 32);

    pInfo->flags = 0; /* must be zero */

    /* libraryDescription and libraryVersion are new for v2.0 */
    CryptoPP::memcpy_s((char*)pInfo->libraryDescription,32,"CIE PKCS11\0                     ", 32);

    pInfo->libraryVersion.major = 1; /* version of library */
    pInfo->libraryVersion.minor = 0; /* version of library */

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetFunctionList(CK_FUNCTION_LIST_PTR_PTR ppFunctionList)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(ppFunctionList)

    logParam(ppFunctionList)

    if (ppFunctionList == NULL)
        throw p11_error(CKR_ARGUMENTS_BAD);

    static CK_FUNCTION_LIST functionList = {{ 2, 20},
        C_Initialize,
        C_Finalize,
        C_GetInfo,
        C_GetFunctionList,
        C_GetSlotList,
        C_GetSlotInfo,
        C_GetTokenInfo,
        C_GetMechanismList,
        C_GetMechanismInfo,
        C_InitToken,
        C_InitPIN,
        C_SetPIN,
        C_OpenSession,
        C_CloseSession,
        C_CloseAllSessions,
        C_GetSessionInfo,
        C_GetOperationState,
        C_SetOperationState,
        C_Login,
        C_Logout,
        C_CreateObject,
        C_CopyObject,
        C_DestroyObject,
        C_GetObjectSize,
        C_GetAttributeValue,
        C_SetAttributeValue,
        C_FindObjectsInit,
        C_FindObjects,
        C_FindObjectsFinal,
        C_EncryptInit,
        C_Encrypt,
        C_EncryptUpdate,
        C_EncryptFinal,
        C_DecryptInit,
        C_Decrypt,
        C_DecryptUpdate,
        C_DecryptFinal,
        C_DigestInit,
        C_Digest,
        C_DigestUpdate, C_DigestKey, C_DigestFinal, C_SignInit, C_Sign,
        C_SignUpdate, C_SignFinal, C_SignRecoverInit, C_SignRecover,
        C_VerifyInit, C_Verify, C_VerifyUpdate, C_VerifyFinal,
        C_VerifyRecoverInit, C_VerifyRecover, C_DigestEncryptUpdate,
        C_DecryptDigestUpdate, C_SignEncryptUpdate, C_DecryptVerifyUpdate,
        C_GenerateKey, C_GenerateKeyPair, C_WrapKey, C_UnwrapKey,
        C_DeriveKey, C_SeedRandom, C_GenerateRandom, C_GetFunctionStatus,
        C_CancelFunction, C_WaitForSlotEvent
    };

    *ppFunctionList = &functionList;

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_CreateObject(CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount, CK_OBJECT_HANDLE_PTR phObject)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);


//	checkOutPtr(phObject)

    logParam(hSession)
    logParam(pTemplate)
    logParam(ulCount)
    logParam(phObject)

    WriteAttributes(pTemplate, ulCount);

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    *phObject = pSession->CreateObject(pTemplate, ulCount);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GenerateKey(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount, CK_OBJECT_HANDLE_PTR phKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)
//		checkOutPtr(phKey)

    logParam(hSession)
    logParam(pMechanism)
    logParam(pTemplate)
    logParam(ulCount)
    logParam(phKey)

    WriteAttributes(pTemplate, ulCount);

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    *phKey = pSession->GenerateKey(pMechanism, pTemplate, ulCount);
    return CKR_OK;

    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GenerateKeyPair(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_ATTRIBUTE_PTR pPublicKeyTemplate, CK_ULONG ulPublicKeyAttributeCount, CK_ATTRIBUTE_PTR pPrivateKeyTemplate, CK_ULONG ulPrivateKeyAttributeCount, CK_OBJECT_HANDLE_PTR phPublicKey, CK_OBJECT_HANDLE_PTR phPrivateKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)
//		checkOutPtr(phPublicKey)
//		checkOutPtr(phPrivateKey)

    logParam(hSession)
    logParam(pMechanism)
    logParam(pPublicKeyTemplate)
    logParam(pPrivateKeyTemplate)
    logParam(ulPublicKeyAttributeCount)
    logParam(ulPrivateKeyAttributeCount)
    logParam(phPublicKey)
    logParam(phPrivateKey)


    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    pSession->GenerateKeyPair(pMechanism, pPublicKeyTemplate, ulPublicKeyAttributeCount, pPrivateKeyTemplate, ulPrivateKeyAttributeCount, phPublicKey, phPrivateKey);
    return CKR_OK;

    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DestroyObject(CK_SESSION_HANDLE hSession,CK_OBJECT_HANDLE hObject)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(hSession)
    logParam(hObject)


    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    pSession->DestroyObject(hObject);
    return CKR_OK;

    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DigestInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)

    logParam(hSession)
    logParam(pMechanism)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!CheckMechanismParam(pMechanism))
        throw p11_error(CKR_MECHANISM_PARAM_INVALID);

    pSession->DigestInit(pMechanism);
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Digest(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pDigest, CK_ULONG_PTR pulDigestLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pData, ulDataLen)
//		checkOutArray(pDigest, pulDigestLen)

    logParam(hSession)
    logParamBuf(pData, ulDataLen)
    logParamBuf(pDigest, pulDigestLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray Digest(pDigest, *pulDigestLen);
    ByteArray input(pData, ulDataLen);
    pSession->Digest(input, Digest);
    *pulDigestLen = (CK_ULONG)Digest.size();

//	ByteDynArray Digest = ByteArray(pDigest, *pulDigestLen);
//    ByteArray input(pData, ulDataLen);
//	pSession->Digest(input, Digest);
//	*pulDigestLen = (CK_ULONG)Digest.size();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DigestFinal(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pDigest, CK_ULONG_PTR pulDigestLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutArray(pDigest, pulDigestLen)

    logParam(hSession)
    logParamBuf(pDigest, pulDigestLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);

    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteDynArray Digest = ByteArray(pDigest, *pulDigestLen);
    pSession->DigestFinal(Digest);
    *pulDigestLen = (CK_ULONG)Digest.size();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DigestUpdate (CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pPart, ulPartLen)

    logParam(hSession)
    logParamBuf(pPart, ulPartLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray input(pPart, ulPartLen);
    pSession->DigestUpdate(input);
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_FindObjects(CK_SESSION_HANDLE hSession,CK_OBJECT_HANDLE_PTR phObject,CK_ULONG ulMaxObjectCount,CK_ULONG_PTR pulObjectCount)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutBuffer(phObject, sizeof(CK_OBJECT_HANDLE)*ulMaxObjectCount)

    logParam(hSession)
    logParam(phObject)
    logParam(ulMaxObjectCount)
    logParam(pulObjectCount)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (phObject == NULL && ulMaxObjectCount > 0)
        throw p11_error(CKR_ARGUMENTS_BAD);

    pSession->FindObjects(phObject, ulMaxObjectCount, pulObjectCount);

    Log.write("Objects found: %d", *pulObjectCount);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_FindObjectsFinal(CK_SESSION_HANDLE hSession)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(hSession)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    pSession->FindObjectsFinal();
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}
CK_RV CK_ENTRY C_FindObjectsInit(CK_SESSION_HANDLE hSession, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInArray(pTemplate,ulCount)

    logParam(hSession)
    logParam(pTemplate)
    logParam(ulCount)

    WriteAttributes(pTemplate, ulCount);

//    if (Log.LogParam) {
//        for (DWORD i=0;i<ulCount;i++) {
//            Log.writePure("Template %i:",i+1);
//            Log.writePure("Type: %s (%x)",getAttributeName(pTemplate[i].type),pTemplate[i].type);
//        }
//    }

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (pTemplate == NULL && ulCount > 0)
        throw p11_error(CKR_ARGUMENTS_BAD);

    pSession->FindObjectsInit(pTemplate, ulCount);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetAttributeValue(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulCount )
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInArray(pTemplate, ulCount)

    logParam(hSession)
    logParam(hObject)
    logParam(pTemplate)
    logParam(ulCount)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession==nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    Log.write("In template");
    WriteAttributes(pTemplate, ulCount);

    CK_RV rv = pSession->GetAttributeValue(hObject, pTemplate, ulCount);

//    if (Log.LogParam) {
//        for (DWORD i=0;i<ulCount;i++) {
//            Log.writePure("Template %i:",i+1);
//            Log.writePure("Type: %s (%x)",getAttributeName(pTemplate[i].type),pTemplate[i].type);
//        }
//    }

    Log.write("Out template");
    WriteAttributes(pTemplate, ulCount);

    Log.writePure("return %x", rv);

    return rv;

    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetMechanismList(CK_SLOT_ID slotID, CK_MECHANISM_TYPE_PTR pMechanismList, CK_ULONG_PTR pulCount)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutArray(pMechanismList,pulCount)

    logParam(slotID)
    logParam(pMechanismList)
    logParam(pulCount)

    DWORD dwNumMechansms=sizeof(P11mechanisms)/sizeof(CK_MECHANISM_TYPE);

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSlot> pSlot = CSlot::GetSlotFromID(slotID);
    if (pSlot == nullptr)
        throw p11_error(CKR_SLOT_ID_INVALID);

    if (pMechanismList == nullptr) {
        *pulCount = dwNumMechansms;
        return CKR_OK;
    }

    if (*pulCount >= dwNumMechansms) {
        CryptoPP::memcpy_s(pMechanismList, dwNumMechansms * sizeof(CK_MECHANISM_TYPE), P11mechanisms, dwNumMechansms * sizeof(CK_MECHANISM_TYPE));
        return CKR_OK;
    } else
        throw p11_error(CKR_BUFFER_TOO_SMALL);

    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetMechanismInfo(CK_SLOT_ID slotID, CK_MECHANISM_TYPE type, CK_MECHANISM_INFO_PTR pInfo)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(slotID)
    logParam(type)
    logParam(pInfo)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSlot> pSlot=CSlot::GetSlotFromID(slotID);

    if (pSlot == nullptr)
        throw p11_error(CKR_SLOT_ID_INVALID);

    switch (type) {
    case CKM_RSA_PKCS:
        pInfo->flags = CKF_HW | CKF_ENCRYPT | CKF_DECRYPT | CKF_SIGN | CKF_VERIFY | CKF_SIGN_RECOVER | CKF_VERIFY_RECOVER;
        pInfo->ulMinKeySize = 1024;
        pInfo->ulMaxKeySize = 2048;
        break;
//		case CKM_RSA_X_509:
//			pInfo->flags = CKF_HW | CKF_ENCRYPT | CKF_DECRYPT | CKF_SIGN | CKF_VERIFY | CKF_SIGN_RECOVER | CKF_VERIFY_RECOVER;
//			pInfo->ulMinKeySize = 1024;
//			pInfo->ulMaxKeySize = 2048;
//			break;
    case CKM_SHA1_RSA_PKCS:
    case CKM_SHA256_RSA_PKCS:
        pInfo->flags = CKF_HW | CKF_SIGN | CKF_VERIFY;
        pInfo->ulMinKeySize = 1024;
        pInfo->ulMaxKeySize = 2048;
        break;
    case CKM_MD5_RSA_PKCS:
        pInfo->flags = CKF_HW | CKF_SIGN | CKF_VERIFY;
        pInfo->ulMinKeySize = 1024;
        pInfo->ulMaxKeySize = 2048;
        break;
    case CKM_MD5:
        pInfo->flags = CKF_DIGEST;
        pInfo->ulMinKeySize = 0;
        pInfo->ulMaxKeySize = 0;
        break;
    case CKM_SHA_1:
        pInfo->flags = CKF_DIGEST;
        pInfo->ulMinKeySize = 0;
        pInfo->ulMaxKeySize = 0;
    case CKM_SHA256:
        pInfo->flags = CKF_DIGEST;
        pInfo->ulMinKeySize = 0;
        pInfo->ulMaxKeySize = 0;
        break;
    default:
        throw p11_error(CKR_MECHANISM_INVALID);
    }
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetSessionInfo(CK_SESSION_HANDLE hSession, CK_SESSION_INFO_PTR pInfo)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(pInfo)

    logParam(hSession)
    logParam(pInfo)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    pInfo->slotID = pSession->pSlot->hSlot;

    if (pSession->pSlot->User==CKU_NOBODY) {
        if (pSession->flags & CKF_RW_SESSION)
            pInfo->state=CKS_RW_PUBLIC_SESSION;
        else
            pInfo->state=CKS_RO_PUBLIC_SESSION;
    } else if (pSession->pSlot->User==CKU_USER) {
        if (pSession->flags & CKF_RW_SESSION)
            pInfo->state=CKS_RW_USER_FUNCTIONS;
        else
            pInfo->state=CKS_RO_USER_FUNCTIONS;
    } else
        pInfo->state=CKS_RW_SO_FUNCTIONS;

    if (pSession->flags & CKF_RW_SESSION)
        pInfo->flags = CKF_RW_SESSION;
    else
        pInfo->flags = 0;

    pInfo->flags = 	pInfo->flags | CKF_SERIAL_SESSION;

    pInfo->ulDeviceError = 0;

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Login(CK_SESSION_HANDLE hSession, CK_USER_TYPE userType, CK_CHAR_PTR pPin, CK_ULONG ulPinLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pPin, ulPinLen)

    logParam(hSession)
    logParam(userType)
    logParamBufHide(pPin, ulPinLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (userType != CKU_SO && userType != CKU_USER)
        throw p11_error(CKR_USER_TYPE_INVALID);

    pSession->Login(userType, pPin, ulPinLen);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Logout(CK_SESSION_HANDLE hSession)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(hSession)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (pSession->pSlot->User == CKU_NOBODY)
        throw p11_error(CKR_USER_NOT_LOGGED_IN);

    pSession->Logout();
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SetAttributeValue(CK_SESSION_HANDLE hSession,CK_OBJECT_HANDLE hObject,CK_ATTRIBUTE_PTR pTemplate,CK_ULONG ulCount)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

    logParam(hSession)
    logParam(hObject)
    logParam(pTemplate)
    logParam(ulCount)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    pSession->SetAttributeValue(hObject, pTemplate, ulCount);
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;

}

CK_RV CK_ENTRY C_Sign(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature, CK_ULONG_PTR pulSignatureLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pData, ulDataLen)
//		checkOutArray(pSignature, pulSignatureLen)
//		checkInBuffer(pData, ulDataLen)
//		checkOutArray(pSignature, pulSignatureLen)

    logParam(hSession)
    logParamBuf(pData, ulDataLen)
    logParamBuf(pSignature, pulSignatureLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray Signature(pSignature,*pulSignatureLen);
    ByteArray input(pData, ulDataLen);
    pSession->Sign(input, Signature);
    *pulSignatureLen = (CK_ULONG)Signature.size();
    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SignFinal(CK_SESSION_HANDLE hSession,CK_BYTE_PTR pSignature,CK_ULONG_PTR pulSignatureLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutArray(pSignature, pulSignatureLen)

    logParam(hSession)
    logParamBuf(pSignature, pulSignatureLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!pSession->pSignMechanism->SignSupportMultipart())
        throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);

    ByteArray Signature(pSignature,*pulSignatureLen);
    pSession->SignFinal(Signature);
    *pulSignatureLen = (CK_ULONG)Signature.size();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SignInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)

    logParam(hSession)
    logParam(pMechanism)
    logParam(hKey)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);


    if (!CheckMechanismParam(pMechanism))
        throw p11_error(CKR_MECHANISM_PARAM_INVALID);

    pSession->SignInit(pMechanism, hKey);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}
CK_RV CK_ENTRY C_SignUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pPart, ulPartLen)

    logParam(hSession)
    logParamBuf(pPart, ulPartLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!pSession->pSignMechanism->SignSupportMultipart())
        throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);

    ByteArray input(pPart, ulPartLen);
    pSession->SignUpdate(input);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SignRecoverInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)

    logParam(hSession)
    logParam(pMechanism)
    logParam(hKey)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!CheckMechanismParam(pMechanism))
        throw p11_error(CKR_MECHANISM_PARAM_INVALID);

    pSession->SignRecoverInit(pMechanism, hKey);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SignRecover(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature, CK_ULONG_PTR pulSignatureLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pData, ulDataLen)
//		checkOutArray(pSignature, pulSignatureLen)

    logParam(hSession)
    logParamBuf(pData, ulDataLen)
    logParamBuf(pSignature, pulSignatureLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray Signature(pSignature,*pulSignatureLen);
    ByteArray input(pData, ulDataLen);
    pSession->SignRecover(input, Signature);
    *pulSignatureLen = (CK_ULONG)Signature.size();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_VerifyRecoverInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)

    logParam(hSession)
    logParam(pMechanism)
    logParam(hKey)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);


    if (!CheckMechanismParam(pMechanism))
        throw p11_error(CKR_MECHANISM_PARAM_INVALID);

    pSession->VerifyRecoverInit(pMechanism, hKey);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_VerifyRecover(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pSignature, CK_ULONG ulSignatureLen, CK_BYTE_PTR pData, CK_ULONG_PTR pulDataLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutArray(pData, pulDataLen)
//		checkInBuffer(pSignature, ulSignatureLen)

    logParam(hSession)
    logParamBuf(pSignature, ulSignatureLen)
    logParamBuf(pData, pulDataLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray Data(pData,*pulDataLen);
    ByteArray input(pSignature, ulSignatureLen);
    pSession->VerifyRecover(input, Data);
    *pulDataLen = (CK_ULONG)Data.size();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_VerifyInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInPtr(pMechanism)

    logParam(hSession)
    logParam(pMechanism)
    logParam(hKey)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!CheckMechanismParam(pMechanism))
        throw p11_error(CKR_MECHANISM_PARAM_INVALID);

    pSession->VerifyInit(pMechanism, hKey);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Verify(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pSignature, CK_ULONG ulSignatureLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pData, ulDataLen)
//		checkInBuffer(pSignature, ulSignatureLen)

    logParam(hSession)
    logParamBuf(pData, ulDataLen)
    logParamBuf(pSignature, ulSignatureLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray input(pData, ulDataLen);
    ByteArray signature(pSignature, ulSignatureLen);
    pSession->Verify(input, signature);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_VerifyUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pData, ulDataLen)

    logParam(hSession)
    logParamBuf(pData, ulDataLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!pSession->pVerifyMechanism->VerifySupportMultipart())
        throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);

    ByteArray input(pData, ulDataLen);
    pSession->VerifyUpdate(input);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_VerifyFinal(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pSignature, CK_ULONG ulSignatureLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pSignature, ulSignatureLen)

    logParam(hSession)
    logParamBuf(pSignature, ulSignatureLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (!pSession->pVerifyMechanism->VerifySupportMultipart())
        throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);

    ByteArray input(pSignature, ulSignatureLen);
    pSession->VerifyFinal(input);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Encrypt(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pData, CK_ULONG ulDataLen, CK_BYTE_PTR pEncryptedData, CK_ULONG_PTR pulEncryptedDataLen)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkInBuffer(pData, ulDataLen)
////		checkOutArray(pEncryptedData, pulEncryptedDataLen)
//
//		logParam(hSession)
//		logParamBuf(pData, ulDataLen)
//		logParamBuf(pEncryptedData, pulEncryptedDataLen)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//			std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	ByteArray EncryptedData(pEncryptedData, *pulEncryptedDataLen);
//    ByteArray input(pData, ulDataLen);
//	pSession->Encrypt(input, EncryptedData);
//	*pulEncryptedDataLen = (CK_ULONG)EncryptedData.size();
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_EncryptFinal(CK_SESSION_HANDLE hSession,CK_BYTE_PTR pEncryptedData,CK_ULONG_PTR pulEncryptedDataLen)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkOutArray(pEncryptedData, pulEncryptedDataLen)
//
//		logParam(hSession)
//		logParamBuf(pEncryptedData, pulEncryptedDataLen)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession = CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	if (!pSession->pEncryptMechanism->EncryptSupportMultipart())
//		throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);
//
//	ByteArray EncryptedData(pEncryptedData, *pulEncryptedDataLen);
//	pSession->EncryptFinal(EncryptedData);
//	*pulEncryptedDataLen = (CK_ULONG)EncryptedData.size();
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_EncryptInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkInPtr(pMechanism)
//
//		logParam(hSession)
//		logParam(pMechanism)
//		logParam(hKey)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	if (!CheckMechanismParam(pMechanism))
//		throw p11_error(CKR_MECHANISM_PARAM_INVALID);
//
//	pSession->EncryptInit(pMechanism, hKey);
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_EncryptUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen, CK_BYTE_PTR pEncryptedPart, CK_ULONG_PTR pulEncryptedPartLen)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkInBuffer(pPart, ulPartLen)
////		checkOutArray(pEncryptedPart, pulEncryptedPartLen)
//
//		logParam(hSession)
//		logParamBuf(pPart, ulPartLen)
//		logParamBuf(pEncryptedPart, pulEncryptedPartLen)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	if (!pSession->pEncryptMechanism->EncryptSupportMultipart())
//		throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);
//
//	ByteArray EncryptedPart(pEncryptedPart, *pulEncryptedPartLen);
//    ByteArray input(pPart, ulPartLen);
//	pSession->EncryptUpdate(input, EncryptedPart);
//	*pulEncryptedPartLen = (CK_ULONG)EncryptedPart.size();
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_Decrypt(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pEncryptedData, CK_ULONG ulEncryptedDataLen, CK_BYTE_PTR pData, CK_ULONG_PTR pulDataLen)
{
    init_p11_func
    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkInBuffer(pEncryptedData, ulEncryptedDataLen)
////		checkOutArray(pData, pulDataLen)
//
//		logParam(hSession)
//		logParamBuf(pEncryptedData, ulEncryptedDataLen)
//		logParamBuf(pData, pulDataLen)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	ByteArray Data(pData, *pulDataLen);
//    ByteArray input(pEncryptedData, ulEncryptedDataLen);
//	pSession->Decrypt(input, Data);
//	*pulDataLen = (CK_ULONG)Data.size();
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DecryptFinal(CK_SESSION_HANDLE hSession,CK_BYTE_PTR pData,CK_ULONG_PTR pulDataLen)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkOutArray(pData, pulDataLen)
//
//		logParam(hSession)
//		logParamBuf(pData, pulDataLen)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	if (!pSession->pDecryptMechanism->DecryptSupportMultipart())
//		throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);
//
//	ByteArray Data(pData, *pulDataLen);
//	pSession->DecryptFinal(Data);
//	*pulDataLen = (CK_ULONG)Data.size();
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DecryptInit(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hKey)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkInPtr(pMechanism)
//
//		logParam(hSession)
//		logParam(pMechanism)
//		logParam(hKey)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//
//	if (!CheckMechanismParam(pMechanism))
//		throw p11_error(CKR_MECHANISM_PARAM_INVALID);
//
//	pSession->DecryptInit(pMechanism, hKey);
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_DecryptUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pEncryptedPart, CK_ULONG ulEncryptedPartLen, CK_BYTE_PTR pPart, CK_ULONG_PTR pulPartLen)
{
    init_p11_func

    return CKR_FUNCTION_NOT_SUPPORTED;

//	std::unique_lock<std::mutex> lock(p11Mutex);
//
////	checkInBuffer(pEncryptedPart, ulEncryptedPartLen)
////		checkOutArray(pPart, pulPartLen)
//
//		logParam(hSession)
//		logParamBuf(pEncryptedPart, ulEncryptedPartLen)
//		logParamBuf(pPart, pulPartLen)
//
//		if (!bP11Initialized)
//			throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
//
//	std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);
//
//	if (pSession == nullptr)
//		throw p11_error(CKR_SESSION_HANDLE_INVALID);
//
//	if (!pSession->pDecryptMechanism->DecryptSupportMultipart())
//		throw p11_error(CKR_KEY_FUNCTION_NOT_PERMITTED);
//
//	ByteArray Part(pPart, *pulPartLen);
//    ByteArray input(pEncryptedPart, ulEncryptedPartLen);
//	pSession->DecryptUpdate(input, Part);
//	*pulPartLen = (CK_ULONG)Part.size();
//
//	return CKR_OK;
    exit_p11_func
//	return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_WaitForSlotEvent(CK_FLAGS flags, CK_SLOT_ID_PTR pSlot, CK_VOID_PTR pReserved)
{
    init_p11_func

//		checkOutPtr(pSlot)

    logParam(flags)
    logParam(pSlot)
    logParam(pReserved)

    if (pReserved != NULL)
        throw p11_error(CKR_ARGUMENTS_BAD);

    if (flags != 0 && flags != CKF_DONT_BLOCK)
        throw p11_error(CKR_ARGUMENTS_BAD);

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    if (flags & CKF_DONT_BLOCK) {
        std::unique_lock<std::mutex> lock(p11Mutex);
        //CSyncroLocker lock(p11EventMutex);
        SlotMap::iterator it=CSlot::g_mSlots.begin();
        while (it!=CSlot::g_mSlots.end()) {
            if (it->second->lastEvent!=SE_NoEvent) {
                *pSlot=it->second->hSlot;
                it->second->lastEvent=SE_NoEvent;
                return CKR_OK;
            }
            it++;
        }
        throw p11_error(CKR_NO_EVENT);
    }

    while (1) {
        p11slotEvent.wait();
        //CSyncroLocker lock(p11EventMutex);
        if (!bP11Initialized) {
            *pSlot=0;
            throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);
        }
        SlotMap::iterator it=CSlot::g_mSlots.begin();
        while (it!=CSlot::g_mSlots.end()) {
            if (it->second->lastEvent!=SE_NoEvent) {
                *pSlot=it->second->hSlot;
                it->second->lastEvent=SE_NoEvent;
                return CKR_OK;
            }
            it++;
        }
    }

    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SeedRandom(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pSeed, CK_ULONG ulSeedLen)
{
    init_p11_func

    logParam(hSession)
    logParamBuf(pSeed, ulSeedLen)

    throw p11_error(CKR_RANDOM_SEED_NOT_SUPPORTED);
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GenerateRandom(CK_SESSION_HANDLE hSession, CK_BYTE_PTR RandomData, CK_ULONG ulRandomLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutBuffer(RandomData, ulRandomLen)

    logParam(hSession)
    logParamBuf(RandomData, ulRandomLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray input(RandomData, ulRandomLen);
    pSession->GenerateRandom(input);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_InitPIN(CK_SESSION_HANDLE hSession,CK_CHAR_PTR pPin,CK_ULONG ulPinLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pPin,ulPinLen);

    logParam(hSession)
    logParamBufHide(pPin, ulPinLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray input(pPin, ulPinLen);
    pSession->InitPIN(input);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SetPIN(CK_SESSION_HANDLE hSession,CK_CHAR_PTR pOldPin,CK_ULONG ulOldLen,CK_CHAR_PTR pNewPin,CK_ULONG ulNewLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pOldPin,ulOldLen);
//	checkInBuffer(pNewPin,ulNewLen);

    logParam(hSession)
    logParamBufHide(pOldPin, ulOldLen)
    logParamBufHide(pNewPin, ulNewLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray inputOld(pOldPin, ulOldLen);
    ByteArray inputNew(pNewPin, ulNewLen);
    pSession->SetPIN(inputOld, inputNew);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetObjectSize(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hObject,CK_ULONG_PTR pulSize)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutPtr(pulSize);

    logParam(hSession)
    logParam(hObject)
    logParam(pulSize)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    *pulSize = pSession->GetObjectSize(hObject);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_GetOperationState(CK_SESSION_HANDLE hSession,CK_BYTE_PTR pOperationState,CK_ULONG_PTR pulOperationStateLen)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkOutArray(pOperationState, pulOperationStateLen)

    logParam(hSession)
    logParamBuf(pOperationState, pulOperationStateLen)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    ByteArray OperationState(pOperationState,*pulOperationStateLen);
    pSession->GetOperationState(OperationState);
    *pulOperationStateLen = (CK_ULONG)OperationState.size();

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

CK_RV CK_ENTRY C_SetOperationState(CK_SESSION_HANDLE hSession,CK_BYTE_PTR pOperationState,CK_ULONG ulOperationStateLen,CK_OBJECT_HANDLE hEncryptionKey,CK_OBJECT_HANDLE hAuthenticationKey)
{
    init_p11_func
    std::unique_lock<std::mutex> lock(p11Mutex);

//	checkInBuffer(pOperationState, ulOperationStateLen)

    logParam(hSession)
    logParamBuf(pOperationState, ulOperationStateLen)
    logParam(hEncryptionKey)
    logParam(hAuthenticationKey)

    if (!bP11Initialized)
        throw p11_error(CKR_CRYPTOKI_NOT_INITIALIZED);

    std::shared_ptr<CSession> pSession	=CSession::GetSessionFromID(hSession);

    if (pSession == nullptr)
        throw p11_error(CKR_SESSION_HANDLE_INVALID);

    if (hEncryptionKey != CK_INVALID_HANDLE)
        throw p11_error(CKR_KEY_NOT_NEEDED);
    if (hAuthenticationKey != CK_INVALID_HANDLE)
        throw p11_error(CKR_KEY_NOT_NEEDED);

    ByteArray input(pOperationState, ulOperationStateLen);
    pSession->SetOperationState(input);

    return CKR_OK;
    exit_p11_func
    return CKR_GENERAL_ERROR;
}

#define unsupported \
{ \
	init_p11_func \
	Log.write("%s","Funzione non supportata!!"); \
	throw p11_error(CKR_FUNCTION_NOT_SUPPORTED); \
	exit_p11_func \
	return CKR_FUNCTION_NOT_SUPPORTED; \
}

CK_RV CK_ENTRY C_InitToken(CK_SLOT_ID slotID,CK_CHAR_PTR pPin,CK_ULONG ulPinLen,CK_UTF8CHAR_PTR pLabel) unsupported
CK_RV CK_ENTRY C_CopyObject(CK_SESSION_HANDLE hSession,CK_OBJECT_HANDLE hObject,CK_ATTRIBUTE_PTR pTemplate,CK_ULONG ulCount,CK_OBJECT_HANDLE_PTR phNewObject) unsupported
CK_RV CK_ENTRY C_DigestKey(CK_SESSION_HANDLE hSession, CK_OBJECT_HANDLE hKey) unsupported
CK_RV CK_ENTRY C_DigestEncryptUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen, CK_BYTE_PTR pEncryptedPart, CK_ULONG_PTR pulEncryptedPartLen) unsupported
CK_RV CK_ENTRY C_DecryptDigestUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pEncryptedPart, CK_ULONG ulEncryptedPartLen, CK_BYTE_PTR pPart, CK_ULONG_PTR pulPartLen) unsupported
CK_RV CK_ENTRY C_SignEncryptUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pPart, CK_ULONG ulPartLen, CK_BYTE_PTR pEncryptedPart, CK_ULONG_PTR pulEncryptedPartLen) unsupported
CK_RV CK_ENTRY C_DecryptVerifyUpdate(CK_SESSION_HANDLE hSession, CK_BYTE_PTR pEncryptedPart, CK_ULONG ulEncryptedPartLen, CK_BYTE_PTR pPart, CK_ULONG_PTR pulPartLen) unsupported
CK_RV CK_ENTRY C_WrapKey(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hWrappingKey, CK_OBJECT_HANDLE hKey, CK_BYTE_PTR pWrappedKey, CK_ULONG_PTR pulWrappedKeyLen) unsupported
CK_RV CK_ENTRY C_UnwrapKey(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hUnwrappingKey, CK_BYTE_PTR pWrappedKey, CK_ULONG ulWrappedKeyLen, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulAttributeCount, CK_OBJECT_HANDLE_PTR phKey) unsupported
CK_RV CK_ENTRY C_DeriveKey(CK_SESSION_HANDLE hSession, CK_MECHANISM_PTR pMechanism, CK_OBJECT_HANDLE hBaseKey, CK_ATTRIBUTE_PTR pTemplate, CK_ULONG ulAttributeCount, CK_OBJECT_HANDLE_PTR phKey) unsupported
CK_RV CK_ENTRY C_GetFunctionStatus(CK_SESSION_HANDLE hSession) unsupported
CK_RV CK_ENTRY C_CancelFunction(CK_SESSION_HANDLE hSession) unsupported


const char *getAttributeName(unsigned long dwId)
{
    switch (dwId) {
    case 0x00000000:
        return("CKA_CLASS");
    case 0x00000001:
        return("CKA_TOKEN");
    case 0x00000002:
        return("CKA_PRIVATE");
    case 0x00000003:
        return("CKA_LABEL");
    case 0x00000010:
        return("CKA_APPLICATION");
    case 0x00000011:
        return("CKA_VALUE");
    case 0x00000012:
        return("CKA_OBJECT_ID");
    case 0x00000080:
        return("CKA_CERTIFICATE_TYPE");
    case 0x00000081:
        return("CKA_ISSUER");
    case 0x00000082:
        return("CKA_SERIAL_NUMBER");
    case 0x00000083:
        return("CKA_AC_ISSUER");
    case 0x00000084:
        return("CKA_OWNER");
    case 0x00000085:
        return("CKA_ATTR_TYPES");
    case 0x00000086:
        return("CKA_TRUSTED");
    case 0x00000100:
        return("CKA_KEY_TYPE");
    case 0x00000101:
        return("CKA_SUBJECT");
    case 0x00000102:
        return("CKA_ID");
    case 0x00000103:
        return("CKA_SENSITIVE");
    case 0x00000104:
        return("CKA_ENCRYPT");
    case 0x00000105:
        return("CKA_DECRYPT");
    case 0x00000106:
        return("CKA_WRAP");
    case 0x00000107:
        return("CKA_UNWRAP");
    case 0x00000108:
        return("CKA_SIGN");
    case 0x00000109:
        return("CKA_SIGN_RECOVER");
    case 0x0000010A:
        return("CKA_VERIFY");
    case 0x0000010B:
        return("CKA_VERIFY_RECOVER");
    case 0x0000010C:
        return("CKA_DERIVE");
    case 0x00000110:
        return("CKA_START_DATE");
    case 0x00000111:
        return("CKA_END_DATE");
    case 0x00000120:
        return("CKA_MODULUS");
    case 0x00000121:
        return("CKA_MODULUS_BITS");
    case 0x00000122:
        return("CKA_PUBLIC_EXPONENT");
    case 0x00000123:
        return("CKA_PRIVATE_EXPONENT");
    case 0x00000124:
        return("CKA_PRIME_1");
    case 0x00000125:
        return("CKA_PRIME_2");
    case 0x00000126:
        return("CKA_EXPONENT_1");
    case 0x00000127:
        return("CKA_EXPONENT_2");
    case 0x00000128:
        return("CKA_COEFFICIENT");
    case 0x00000130:
        return("CKA_PRIME");
    case 0x00000131:
        return("CKA_SUBPRIME");
    case 0x00000132:
        return("CKA_BASE");
    case 0x00000133:
        return("CKA_PRIME_BITS");
    case 0x00000134:
        return("CKA_SUB_PRIME_BITS");
    case 0x00000160:
        return("CKA_VALUE_BITS");
    case 0x00000161:
        return("CKA_VALUE_LEN");
    case 0x00000162:
        return("CKA_EXTRACTABLE");
    case 0x00000163:
        return("CKA_LOCAL");
    case 0x00000164:
        return("CKA_NEVER_EXTRACTABLE");
    case 0x00000165:
        return("CKA_ALWAYS_SENSITIVE");
    case 0x00000166:
        return("CKA_KEY_GEN_MECHANISM");
    case 0x00000170:
        return("CKA_MODIFIABLE");
    case 0x00000180:
        return("CKA_EC_PARAMS");
    case 0x00000181:
        return("CKA_EC_POINT");
    case 0x00000200:
        return("CKA_SECONDARY_AUTH");
    case 0x00000201:
        return("CKA_AUTH_PIN_FLAGS");
    case 0x00000300:
        return("CKA_HW_FEATURE_TYPE");
    case 0x00000301:
        return("CKA_RESET_ON_INIT");
    case 0x00000302:
        return("CKA_HAS_RESET");
    case 0x80000000:
        return("CKA_VENDOR_DEFINED");
    case 0x80000001:
        return("CKA_SM");
    case 0x80000002:
        return("CKA_SM_PUB");
    }
    return("UNKNOWN");
}



