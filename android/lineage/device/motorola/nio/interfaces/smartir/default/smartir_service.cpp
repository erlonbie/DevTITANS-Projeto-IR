#include "smartir_service.h"

namespace aidl::devtitans::smartir {
    ndk::ScopedAStatus SmartIRService::connect(int32_t *_aidl_return) {
      *_aidl_return = this->smartir.connect();
      LOG(INFO) << "connect(): " << *_aidl_return;
      return ndk::ScopedAStatus::ok();
    }
    ndk::ScopedAStatus SmartIRService::transmit(int32_t *_aidl_return) {
        *_aidl_return = this->smartir.transmit();
        LOG(INFO) << "transmit(): " << *_aidl_return;
        return ndk::ScopedAStatus::ok();
    }
    ndk::ScopedAStatus SmartIRService::receive(int32_t *_aidl_return) {
        *_aidl_return = this->smartir.receive();
        LOG(INFO) << "receive(): " << *_aidl_return;
        return ndk::ScopedAStatus::ok();
    }
    ndk::ScopedAStatus SmartIRService::set_transmit(int32_t in_irTransmiteValue, bool *_aidl_return) {
      *_aidl_return = this->smartir.set_transmit(in_irTransmiteValue);
      LOG(INFO) << "set_transmit( " << in_irTransmiteValue << "): " << (*_aidl_return ? "true" : "false");
      return ndk::ScopedAStatus::ok();
    }

    ndk::ScopedAStatus SmartIRService::set_receive(int32_t in_irReceiveValue, bool *_aidl_return) {
      *_aidl_return = this->smartir.set_receive(in_irReceiveValue);
      LOG(INFO) << "set_receive( " << in_irReceiveValue << "): " << (*_aidl_return ? "true" : "false");
      return ndk::ScopedAStatus::ok();
    }
}
