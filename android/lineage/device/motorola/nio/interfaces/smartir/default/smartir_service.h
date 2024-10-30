#pragma once

#include <android-base/logging.h>
#include <android/binder_manager.h>
#include <android/binder_process.h>

#include <aidl/devtitans/smartir/BnSmartIR.h>

#include "smart_ir_lib.h"

using namespace devtitans::smartir;

namespace aidl::devtitans::smartir {
class SmartIRService : public BnSmartIR {
 public:
  ::ndk::ScopedAStatus connect(int32_t *_aidl_return) override;
  ::ndk::ScopedAStatus transmit(int32_t *_aidl_return) override;
  ::ndk::ScopedAStatus receive(int32_t *_aidl_return) override;
  ::ndk::ScopedAStatus set_transmit(int32_t in_irTransmiteValue,
                                    bool *_aidl_return) override;
  ::ndk::ScopedAStatus set_receive(int32_t in_irReceiveValue,
                                   bool *_aidl_return) override;

 private:
  SmartIr smartir;  // Biblioteca
};
}  // namespace aidl::devtitans::smartir
