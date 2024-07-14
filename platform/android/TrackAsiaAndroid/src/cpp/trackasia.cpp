#include "trackasia.hpp"

namespace mbgl {
namespace android {

jni::Local<jni::Object<AssetManager>> TrackAsia::getAssetManager(jni::JNIEnv& env) {
    static auto& javaClass = jni::Class<TrackAsia>::Singleton(env);
    auto method = javaClass.GetStaticMethod<jni::Object<AssetManager>()>(env, "getAssetManager");
    return javaClass.Call(env, method);
}

jboolean TrackAsia::hasInstance(jni::JNIEnv& env) {
    static auto& javaClass = jni::Class<TrackAsia>::Singleton(env);
    auto method = javaClass.GetStaticMethod<jboolean()>(env, "hasInstance");
    return javaClass.Call(env, method);
}

void TrackAsia::registerNative(jni::JNIEnv& env) {
    jni::Class<TrackAsia>::Singleton(env);
}

} // namespace android
} // namespace mbgl
