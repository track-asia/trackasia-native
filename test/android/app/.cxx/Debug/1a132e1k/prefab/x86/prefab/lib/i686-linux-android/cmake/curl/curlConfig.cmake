find_package(boringssl REQUIRED CONFIG)

if(NOT TARGET curl::curl_static)
add_library(curl::curl_static STATIC IMPORTED)
set_target_properties(curl::curl_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/curl_static/libs/android.x86/libcurl_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/curl_static/include"
    INTERFACE_LINK_LIBRARIES "-lz;curl::nghttp2_static;curl::nghttp3_static;curl::ngtcp2_static;boringssl::ssl_static"
)
endif()

if(NOT TARGET curl::nghttp2_static)
add_library(curl::nghttp2_static STATIC IMPORTED)
set_target_properties(curl::nghttp2_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/nghttp2_static/libs/android.x86/libnghttp2_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/nghttp2_static/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

if(NOT TARGET curl::nghttp3_static)
add_library(curl::nghttp3_static STATIC IMPORTED)
set_target_properties(curl::nghttp3_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/nghttp3_static/libs/android.x86/libnghttp3_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/nghttp3_static/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

if(NOT TARGET curl::ngtcp2_crypto_static)
add_library(curl::ngtcp2_crypto_static STATIC IMPORTED)
set_target_properties(curl::ngtcp2_crypto_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/ngtcp2_crypto_static/libs/android.x86/libngtcp2_crypto_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/ngtcp2_crypto_static/include"
    INTERFACE_LINK_LIBRARIES "boringssl::ssl_static"
)
endif()

if(NOT TARGET curl::ngtcp2_static)
add_library(curl::ngtcp2_static STATIC IMPORTED)
set_target_properties(curl::ngtcp2_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/ngtcp2_static/libs/android.x86/libngtcp2_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/6a469a578b5fa02df5f6f0c1dbe70d7b/transformed/curl-8.8.0/prefab/modules/ngtcp2_static/include"
    INTERFACE_LINK_LIBRARIES "curl::ngtcp2_crypto_static"
)
endif()

