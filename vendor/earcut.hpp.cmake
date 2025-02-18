if(TARGET mbgl-vendor-earcut.hpp)
    return()
endif()

add_library(
    mbgl-vendor-earcut.hpp INTERFACE
)

target_include_directories(
    mbgl-vendor-earcut.hpp SYSTEM
    INTERFACE ${CMAKE_CURRENT_LIST_DIR}/earcut.hpp/include
)

set_target_properties(
    mbgl-vendor-earcut.hpp
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "earcut.hpp"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/earcut.hpp"
        INTERFACE_TRACKASIA_AUTHOR "Mapbox"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/earcut.hpp/LICENSE
)
