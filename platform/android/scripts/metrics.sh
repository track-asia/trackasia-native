#!/usr/bin/env bash

set -e
set -o pipefail

# Track individual architectures
scripts/check_binary_size.js "TrackasiaGLAndroidSDK/build/intermediates/intermediate-jars/release/jni/armeabi-v7a/libmapbox-gl.so" "Android arm-v7"
scripts/check_binary_size.js "TrackasiaGLAndroidSDK/build/intermediates/intermediate-jars/release/jni/arm64-v8a/libmapbox-gl.so"   "Android arm-v8"
scripts/check_binary_size.js "TrackasiaGLAndroidSDK/build/intermediates/intermediate-jars/release/jni/x86/libmapbox-gl.so"         "Android x86"
scripts/check_binary_size.js "TrackasiaGLAndroidSDK/build/intermediates/intermediate-jars/release/jni/x86_64/libmapbox-gl.so"      "Android x86_64"

# Track overall library size
scripts/check_binary_size.js "TrackasiaGLAndroidSDK/build/outputs/aar/TrackasiaGLAndroidSDK-release.aar" "Android AAR"

if [[ $CIRCLE_BRANCH == main ]]; then
  # Build source data for http://mapbox.github.io/mapbox-gl-native/metrics/binary-size/
  # and log binary sizes to metrics warehouse
  scripts/publish_binary_size.js
fi
