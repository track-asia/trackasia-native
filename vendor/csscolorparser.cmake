if(TARGET mbgl-vendor-csscolorparser)
    return()
endif()

if(MLN_WITH_QT)
    add_library(mbgl-vendor-csscolorparser OBJECT)
else()
    add_library(mbgl-vendor-csscolorparser STATIC)
endif()

target_sources(
    mbgl-vendor-csscolorparser PRIVATE
    ${CMAKE_CURRENT_LIST_DIR}/csscolorparser/csscolorparser.cpp
)

target_link_libraries(
    mbgl-vendor-csscolorparser
    PRIVATE mbgl-compiler-options
)

target_include_directories(
    mbgl-vendor-csscolorparser SYSTEM
    PUBLIC ${CMAKE_CURRENT_LIST_DIR}/csscolorparser
)

if(MSVC)
    target_compile_options(mbgl-vendor-csscolorparser PRIVATE /wd4244)
endif()

set_target_properties(
    mbgl-vendor-csscolorparser
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "csscolorparser"
        INTERFACE_TRACKASIA_URL "https://github.com/mapbox/css-color-parser-cpp"
        INTERFACE_TRACKASIA_AUTHOR "Dean McNamee and Konstantin Käfer"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/csscolorparser/LICENSE
)
