if(TARGET mbgl-vendor-parsedate)
    return()
endif()

if(MLN_WITH_QT)
    add_library(mbgl-vendor-parsedate OBJECT)
else()
    add_library(mbgl-vendor-parsedate STATIC)
endif()

target_sources(
    mbgl-vendor-parsedate PRIVATE
    ${CMAKE_CURRENT_LIST_DIR}/parsedate/parsedate.cpp
)

target_link_libraries(
    mbgl-vendor-parsedate
    PRIVATE mbgl-compiler-options
)

target_include_directories(
    mbgl-vendor-parsedate SYSTEM
    PUBLIC ${CMAKE_CURRENT_LIST_DIR}/parsedate
)

set_target_properties(
    mbgl-vendor-parsedate
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "parsedate"
        INTERFACE_TRACKASIA_URL "https://curl.haxx.se"
        INTERFACE_TRACKASIA_AUTHOR "Daniel Stenberg and others"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/parsedate/COPYING
)
