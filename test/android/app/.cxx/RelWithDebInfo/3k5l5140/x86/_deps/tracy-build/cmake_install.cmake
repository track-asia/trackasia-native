# Install script for directory: /Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "/usr/local")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "RelWithDebInfo")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Install shared libraries without execute permission?
if(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  set(CMAKE_INSTALL_SO_NO_EXE "0")
endif()

# Is this installation the result of a crosscompile?
if(NOT DEFINED CMAKE_CROSSCOMPILING)
  set(CMAKE_CROSSCOMPILING "TRUE")
endif()

# Set default install directory permissions.
if(NOT DEFINED CMAKE_OBJDUMP)
  set(CMAKE_OBJDUMP "/Users/sangnguyen/Library/Android/sdk/ndk/26.1.10909125/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-objdump")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/lib" TYPE STATIC_LIBRARY FILES "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-build/libTracyClient.a")
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/tracy" TYPE FILE FILES
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyC.h"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/Tracy.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyD3D11.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyD3D12.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyLua.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyOpenCL.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyOpenGL.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/tracy/TracyVulkan.hpp"
    )
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/client" TYPE FILE FILES
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/tracy_concurrentqueue.h"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/tracy_rpmalloc.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/tracy_SPSCQueue.h"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyKCore.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyArmCpuTable.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyCallstack.h"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyCallstack.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyCpuid.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyDebug.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyDxt1.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyFastVector.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyLock.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyProfiler.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyRingBuffer.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyScoped.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyStringHelpers.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracySysPower.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracySysTime.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracySysTrace.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/client/TracyThread.hpp"
    )
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/common" TYPE FILE FILES
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/tracy_lz4.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/tracy_lz4hc.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyAlign.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyAlloc.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyApi.h"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyColor.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyForceInline.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyMutex.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyProtocol.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyQueue.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracySocket.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyStackFrames.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracySystem.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyUwp.hpp"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-src/public/common/TracyYield.hpp"
    )
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  if(EXISTS "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/share/Tracy/TracyTargets.cmake")
    file(DIFFERENT EXPORT_FILE_CHANGED FILES
         "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/share/Tracy/TracyTargets.cmake"
         "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-build/CMakeFiles/Export/share/Tracy/TracyTargets.cmake")
    if(EXPORT_FILE_CHANGED)
      file(GLOB OLD_CONFIG_FILES "$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/share/Tracy/TracyTargets-*.cmake")
      if(OLD_CONFIG_FILES)
        message(STATUS "Old export file \"$ENV{DESTDIR}${CMAKE_INSTALL_PREFIX}/share/Tracy/TracyTargets.cmake\" will be replaced.  Removing files [${OLD_CONFIG_FILES}].")
        file(REMOVE ${OLD_CONFIG_FILES})
      endif()
    endif()
  endif()
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/Tracy" TYPE FILE FILES "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-build/CMakeFiles/Export/share/Tracy/TracyTargets.cmake")
  if("${CMAKE_INSTALL_CONFIG_NAME}" MATCHES "^([Rr][Ee][Ll][Ww][Ii][Tt][Hh][Dd][Ee][Bb][Ii][Nn][Ff][Oo])$")
    file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/Tracy" TYPE FILE FILES "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-build/CMakeFiles/Export/share/Tracy/TracyTargets-relwithdebinfo.cmake")
  endif()
endif()

if("x${CMAKE_INSTALL_COMPONENT}x" STREQUAL "xUnspecifiedx" OR NOT CMAKE_INSTALL_COMPONENT)
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/share/Tracy" TYPE FILE FILES "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/RelWithDebInfo/3k5l5140/x86/_deps/tracy-build/TracyConfig.cmake")
endif()

