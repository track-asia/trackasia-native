if(TARGET mbgl-vendor-calloutview)
    return()
endif()

add_library(
    mbgl-vendor-calloutview ${CMAKE_CURRENT_LIST_DIR}/SMCalloutView/SMCalloutView.m
)

target_include_directories(
    mbgl-vendor-calloutview SYSTEM
    INTERFACE ${CMAKE_CURRENT_LIST_DIR}/SMCalloutView
)

set_target_properties(
    mbgl-vendor-calloutview
    PROPERTIES
        INTERFACE_TRACKASIA_NAME "Boost C++ Libraries"
        INTERFACE_TRACKASIA_URL "https://github.com/nfarina/calloutview"
        INTERFACE_TRACKASIA_AUTHOR "Nick Farina"
        INTERFACE_TRACKASIA_LICENSE ${CMAKE_CURRENT_LIST_DIR}/SMCalloutView/LICENSE
)
