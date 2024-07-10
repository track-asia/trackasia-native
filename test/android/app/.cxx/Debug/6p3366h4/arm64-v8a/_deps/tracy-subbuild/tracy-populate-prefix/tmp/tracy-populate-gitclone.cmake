
if(NOT "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-subbuild/tracy-populate-prefix/src/tracy-populate-stamp/tracy-populate-gitinfo.txt" IS_NEWER_THAN "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-subbuild/tracy-populate-prefix/src/tracy-populate-stamp/tracy-populate-gitclone-lastrun.txt")
  message(STATUS "Avoiding repeated git clone, stamp file is up to date: '/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-subbuild/tracy-populate-prefix/src/tracy-populate-stamp/tracy-populate-gitclone-lastrun.txt'")
  return()
endif()

execute_process(
  COMMAND ${CMAKE_COMMAND} -E rm -rf "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-src"
  RESULT_VARIABLE error_code
  )
if(error_code)
  message(FATAL_ERROR "Failed to remove directory: '/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-src'")
endif()

# try the clone 3 times in case there is an odd git clone issue
set(error_code 1)
set(number_of_tries 0)
while(error_code AND number_of_tries LESS 3)
  execute_process(
    COMMAND "/opt/homebrew/bin/git"  clone --no-checkout --depth 1 --no-single-branch --progress --config "advice.detachedHead=false" "https://github.com/wolfpld/tracy.git" "tracy-src"
    WORKING_DIRECTORY "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps"
    RESULT_VARIABLE error_code
    )
  math(EXPR number_of_tries "${number_of_tries} + 1")
endwhile()
if(number_of_tries GREATER 1)
  message(STATUS "Had to git clone more than once:
          ${number_of_tries} times.")
endif()
if(error_code)
  message(FATAL_ERROR "Failed to clone repository: 'https://github.com/wolfpld/tracy.git'")
endif()

execute_process(
  COMMAND "/opt/homebrew/bin/git"  checkout master --
  WORKING_DIRECTORY "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-src"
  RESULT_VARIABLE error_code
  )
if(error_code)
  message(FATAL_ERROR "Failed to checkout tag: 'master'")
endif()

set(init_submodules TRUE)
if(init_submodules)
  execute_process(
    COMMAND "/opt/homebrew/bin/git"  submodule update --recursive --init 
    WORKING_DIRECTORY "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-src"
    RESULT_VARIABLE error_code
    )
endif()
if(error_code)
  message(FATAL_ERROR "Failed to update submodules in: '/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-src'")
endif()

# Complete success, update the script-last-run stamp file:
#
execute_process(
  COMMAND ${CMAKE_COMMAND} -E copy
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-subbuild/tracy-populate-prefix/src/tracy-populate-stamp/tracy-populate-gitinfo.txt"
    "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-subbuild/tracy-populate-prefix/src/tracy-populate-stamp/tracy-populate-gitclone-lastrun.txt"
  RESULT_VARIABLE error_code
  )
if(error_code)
  message(FATAL_ERROR "Failed to copy script-last-run stamp file: '/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a/_deps/tracy-subbuild/tracy-populate-prefix/src/tracy-populate-stamp/tracy-populate-gitclone-lastrun.txt'")
endif()

