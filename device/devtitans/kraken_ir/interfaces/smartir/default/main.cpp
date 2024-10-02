#include "smartir_service.h"

using namespace aidl::devtitans::smartir;                // SmartIRService (smartir_service.cpp)
using namespace std;                                       // std::shared_ptr
using namespace ndk;                                       // ndk::SharedRefBase

int main() {
    LOG(INFO) << "Iniciando SmartIR AIDL Service ...";

    ABinderProcess_setThreadPoolMaxThreadCount(0);

    shared_ptr<SmartIRService> smartir_service = SharedRefBase::make<SmartIRService>();

    const string instance = std::string() + ISmartIR::descriptor + "/default";   // devtitans.smartir.ISmartIR/default
    binder_status_t status = AServiceManager_addService(smartir_service->asBinder().get(), instance.c_str());
    CHECK(status == STATUS_OK);

    LOG(INFO) << "SmartIR AIDL Service iniciado com nome: " << instance;
    ABinderProcess_joinThreadPool();

    return EXIT_FAILURE;                                   // Não deve chegar nunca aqui
}
