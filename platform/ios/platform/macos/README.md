# Trackasia Native for macOS

> **Note**  
> https://github.com/track-asia/trackasia-native/pull/995 introduced OpenGL ES 3.0 shaders which are not supported by macOS without workarounds. That means that while the [Metal backend implementation](https://github.com/track-asia/trackasia-native/blob/main/design-proposals/2022-11-29-metal-port.md) is underway, builds from the `main` branch will not work on macOS. Use the `opengl-2` branch in the meantime.

[![GitHub Action build status](https://github.com/track-asia/trackasia-native/workflows/macos-ci/badge.svg)](https://github.com/track-asia/trackasia-native/actions/workflows/macos-ci.yml) [![GitHub Action build status](https://github.com/track-asia/trackasia-native/workflows/macos-release/badge.svg)](https://github.com/track-asia/trackasia-native/actions/workflows/macos-release.yml)

Put interactive, scalable world maps into your native Cocoa application with Trackasia Native for macOS.

* High-performance OpenGL rendering and multitouch gestures keep your users happy.
* A well-designed, fully documented API helps you stay productive.
* Develop across [multiple platforms](../../README.md), including [iOS](../ios/README.md), using the same styles and similar APIs.

![](docs/img/screenshot.jpg)

Trackasia Native for macOS is compatible with macOS 10.10.0 and above for Cocoa applications developed in Objective-C, Swift, Interface Builder, or AppleScript. For hybrid applications, consider [Trackasia GL JS]([https://github.com/mapbox/mapbox-gl-js/](https://github.com/track-asia/Trackasia-gl-js)).

## Contributing

* [Build the Trackasia Native for macOS from source](INSTALL.md)
* [Contribute to Trackasia Native for macOS](CONTRIBUTING.md)
* [Trackasia Style Spec](https://track-asia.com/Trackasia-style-spec/)
* [Mapbox Vector Tile Specification](https://www.mapbox.com/developers/vector-tiles/)

The Trackasia Organization does not officially support the macOS to the same extent as iOS (see [project tiers](https://github.com/track-asia/Trackasia/blob/main/PROJECT_TIERS.md)); however, bug reports and pull requests are certainly welcome.
