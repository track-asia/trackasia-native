# Trackasia Native for iOS

[![GitHub Action build status](https://github.com/track-asia/trackasia-native/workflows/ios-ci/badge.svg)](https://github.com/track-asia/trackasia-native/actions/workflows/ios-ci.yml) [![GitHub Action build status](https://github.com/track-asia/trackasia-native/workflows/ios-release/badge.svg)](https://github.com/track-asia/trackasia-native/actions/workflows/ios-release.yml)

A library based on [Trackasia Native](https://github.com/track-asia/trackasia-native) for embedding interactive map views with scalable, customizable vector maps into iOS Applications.

## Getting Started

Trackasia Native for iOS is distributed using the [Swift Package Index](https://swiftpackageindex.com/track-asia/trackasia-gl-native-distribution). To add it to your project, follow the steps below.

1. To add a package dependency to your Xcode project, select File > Swift Packages > Add Package Dependency and enter its repository URL. You can also navigate to your target’s General pane, and in the “Frameworks, Libraries, and Embedded Content” section, click the + button, select Add Other, and choose Add Package Dependency.

2. Either add Trackasia GitHub distribution URL `https://github.com/track-asia/trackasia-gl-native-distribution` or search for `trackasia-native` package.

3. Choose "Next". Xcode should clone the distribution repository and download the binaries.

There is a an open bounty to extend this Getting Started guide ([#809](https://github.com/track-asia/trackasia-native/issues/809)). In the meantime, refer to one of these external guides:

- [Get Started with Trackasia Native for iOS using SwiftUI](https://docs.maptiler.com/trackasia-gl-native-ios/ios-swiftui-basic-get-started/)
- [Get Started With Trackasia Native for iOS using UIKit](https://docs.maptiler.com/trackasia-gl-native-ios/ios-uikit-basic-get-started/)

## Developing

### CMake

The main build tool generator supported by Trackasia Native is CMake. There is a `Makefile` which calls CMake and `xcodebuild` under the hood to complete various development tasks, including building with various kinds of configurations and running tests. This `Makefile` can also can set up an Xcode project for Trackasia Native development by copying a `.xcodeproj` file part of the source tree and combining that with the output of CMake.

To set up an Xcode project and open Xcode, use the following command.

```
make iproj
```

### Bazel

The above setup is quite fragile and relies on the `.xcodeproj` files part of the source tree (making changes hard to review). We are looking into a better way to set up an Xcode project for Trackasia Native iOS development. As of May 2023 we are experimenting with [Bazel](https://bazel.build/) together with [rules_xcodeproj](https://github.com/MobileNativeFoundation/rules_xcodeproj). Please [share your experiences](https://github.com/track-asia/trackasia-native/discussions/1145).

You need to install bazelisk, which is a wrapper around Bazel which ensures that the version specified in `.bazelversion` is used.

```
brew install bazelisk
```

Next, you can generate an Xcode project for Trackasia Native development using:

```
bazel run //platform/ios:xcodeproj
```

You can now open `platform/ios/Trackasia.xcodeproj` with Xcode to get started.

It is also possible to build and run the test application in a simulator from the command line without opening Xcode.

```
bazel run //platform/ios:App
```

## Documentation

- [Trackasia Native for iOS API Reference](https://track-asia.com/trackasia-native/ios/api/)