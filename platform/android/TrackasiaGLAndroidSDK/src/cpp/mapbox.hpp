#pragma once

#include "asset_manager.hpp"

#include <jni/jni.hpp>

namespace mbgl {
namespace android {

class Mapbox {
public:
    static constexpr auto Name() { return "com.trackasia.android/Mapbox"; };
    static jboolean hasInstance(jni::JNIEnv&);
    static jni::Local<jni::Object<AssetManager>> getAssetManager(jni::JNIEnv&);
    static void registerNative(jni::JNIEnv&);
};

} // namespace android
} // namespace mbgl