
# Platforms

This page describes the platforms that TrackAsia Native is available on.

## Overview

TrackAsia Native uses a monorepo. Source code for all platforms lives in [`trackasia/trackasia-native`](https://github.com/trackasia/trackasia-native) on GitHub.

| Platform | Source | Notes |
|---|---|---|
| Android | [`platform/android`](https://github.com/trackasia/trackasia-native/tree/main/platform/android) | Integrates with the C++ core via JNI. |
| iOS | [`platform/ios`](https://github.com/trackasia/trackasia-native/tree/main/platform/ios), [`platform/darwin`](https://github.com/trackasia/trackasia-native/tree/main/platform/darwin) | Integrates with the C++ core via Objective-C++.  |
| Linux | [`platform/linux`](https://github.com/trackasia/trackasia-native/tree/main/platform/linux) | Used for development. Also widely used in production for raster tile generation. |
| Windows | [`platform/windows`](https://github.com/trackasia/trackasia-native/tree/main/platform/windows) | |
| macOS | [`platform/macos`](https://github.com/trackasia/trackasia-native/tree/main/platform/macos), [`platform/darwin`](https://github.com/trackasia/trackasia-native/tree/main/platform/darwin)  | Mainly used for development. There is some legacy AppKit code. |
| Node.js | [`platform/node`](https://github.com/trackasia/trackasia-native/tree/main/platform/node) | Uses [NAN](https://github.com/nodejs/nan). Available as [@trackasia/trackasia-gl-native](https://www.npmjs.com/package/@trackasia/trackasia-gl-native) on npm. |
| Qt | [trackasia/trackasia-qt](https://github.com/trackasia/trackasia-native/tree/main/platform/qt), [`platform/qt`](https://github.com/trackasia/trackasia-native)  | Only platform that partially split to another repository. |

Of these, **Android** and **iOS** are considered [core projects](https://github.com/trackasia/trackasia/blob/main/PROJECT_TIERS.md) of the TrackAsia Organization.
### GLFW

You can find an app that uses GLFW in [`platform/glfw`](https://github.com/trackasia/trackasia-native/tree/main/platform/glfw). It works on macOS, Linux and Windows. The app shows an interactive map that can be interacted with. Since GLFW adds relatively little complexity this app is used a lot for development. You can also learn about the C++ API by studying the source code of the GLFW app.

## Rendering Backends

Originally the project only supported OpenGL 2.0. In 2023, the [renderer was modularized](https://github.com/trackasia/trackasia-native/blob/main/design-proposals/2022-10-27-rendering-modularization.md) allowing for the implementation of alternate rendering backends. The first alternate rendering backend that was implemented was [Metal](https://trackasia.org/news/2024-01-19-metal-support-for-trackasia-native-ios-is-here/), followed by [Vulkan](https://trackasia.org/news/2024-12-12-trackasia-android-vulkan/). In the future other rendering backends could be implemented such as WebGPU.

What platforms support which rendering backend can be found below.

| Platform | OpenGL ES 3.0 | Vulkan 1.0 | Metal |
|---|---|---|---|
| Android | ✅ | ✅ | ❌ |
| iOS | ❌ | ❌ | ✅ |
| Linux | ✅ | ✅ | ❌ |
| Windows | ✅ | ❌ | ❌ |
| macOS | ❌ | ✅ | ✅[^1] |
| Node.js | ✅ | ❌ | ✅ [^2] |
| Qt | ✅ | ❌ | ❌ |

[^1]: Requires MoltenVK. Only available when built via CMake.
[^2]: Issue reported, see [#2928](https://github.com/trackasia/trackasia-native/issues/2928).

## Build Tooling

In 2023 we co-opted Bazel as a build tool (generator), mostly due to it having better support for iOS compared to CMake. Some platforms can use CMake as well as Bazel.

| Platform | CMake | Bazel |
|---|---|---|
| Android | ✅ (via Gradle) | ❌ |
| iOS | ❌ | ✅ |
| Linux | ✅ | ✅ |
| Windows | ✅ | ❌ |
| macOS | ✅ | ✅ |
| Node.js | ✅ | ❌ |
| Qt | ✅ | ❌ |
