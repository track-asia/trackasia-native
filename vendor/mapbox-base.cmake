# License helper for mapbox-base, should be upstreamed.

if(NOT TARGET mapbox-base)
    add_subdirectory(${CMAKE_CURRENT_LIST_DIR}/mapbox-base)
endif()

set_target_properties(
    mapbox-base-extras-kdbush.hpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "kdbush.hpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mourner/kdbush.hpp"
        INTERFACE_TRACKASIA_AUTHOR "Vladimir Agafonkin"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/extras/kdbush.hpp/LICENSE
)

set_target_properties(
    mapbox-base-extras-expected-lite
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "expected-lite"
        INTERFACE_TRACKASIA_URL "https://github.com/martinmoene/expected-lite"
        INTERFACE_TRACKASIA_AUTHOR "Martin Moene"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/extras/expected-lite/LICENSE.txt
)

set_target_properties(
    mapbox-base-supercluster.hpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "supercluster.hpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/supercluster.hpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/supercluster.hpp/LICENSE
)

set_target_properties(
    mapbox-base-shelf-pack-cpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "shelf-pack-cpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/shelf-pack-cpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/shelf-pack-cpp/LICENSE.md
)

set_target_properties(
    mapbox-base-geojson-vt-cpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "geojson-vt-cpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/geojson-vt-cpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/geojson-vt-cpp/LICENSE
)

set_target_properties(
    mapbox-base-extras-rapidjson
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "RapidJSON"
        INTERFACE_TRACKASIA_URL "https://rapidjson.org"
        INTERFACE_TRACKASIA_AUTHOR "THL A29 Limited, a Tencent company, and Milo Yip"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/extras/rapidjson/license.txt
)

set_target_properties(
    mapbox-base-geojson.hpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "geojson.hpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/geojson-cpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/geojson.hpp/LICENSE
)

set_target_properties(
    mapbox-base-geometry.hpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "geometry.hpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/geometry.hpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/geometry.hpp/LICENSE
)

set_target_properties(
    mapbox-base
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "mapbox-base"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/mapbox-base"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/LICENSE
)

set_target_properties(
    mapbox-base-variant
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "variant"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/variant"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/variant/LICENSE
)

set_target_properties(
    mapbox-base-cheap-ruler-cpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "cheap-ruler-cpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/cheap-ruler-cpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/mapbox-base/deps/cheap-ruler-cpp/LICENSE
)
