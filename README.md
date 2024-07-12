[![TrackAsia Logo](https://track-asia.com/img/trackasia-logo-big.svg)](https://track-asia.com/)

# TrackAsia GL Native

TrackAsia GL Native is a free and open-source library for publishing maps in your apps and desktop applications on various platforms. Fast displaying of maps is possible thanks to GPU-accelerated vector tile rendering.

This project originated as a fork of Mapbox GL Native, before their switch to a non-OSS license in December 2020. For more information, see: [`FORK.md`](./FORK.md).

<p align="center">
  <img src="https://user-images.githubusercontent.com/649392/211550776-8779041a-7c12-4bed-a7bd-c2ec80af2b29.png" alt="Android device with TrackAsia" width="24%">   <img src="https://user-images.githubusercontent.com/649392/211550762-0f42ebc9-05ab-4d89-bd59-c306453ea9af.png" alt="iOS device with TrackAsia" width="25%">
</p>

## Getting Started

To get started with TrackAsia GL Native, go to your platform below.

## Documentation

The documentation of TrackAsia GL Native is a work in progress. To get an architectural overview and to learn about the current state of the project and its path forward read the [TrackAsia GL Native Markdown Book](https://track-asia.com/trackasia-gl-native/docs/book/). See below for platform-specific documentation.

## Platforms

- [⭐️ Android](platform/android/README.md) 
- [⭐️ iOS](platform/ios/platform/ios/README.md)
- [GLFW](platform/glfw)
- [Linux](platform/linux/README.md)
- [macOS](platform/ios/platform/macos/README.md)
- [Node.js](platform/node/README.md)
- [Qt](platform/qt/README.md)

Platforms with a ⭐️ are **TrackAsia Core Projects** and have a substantial amount of financial resources allocated to them. Learn about the different [project tiers](https://github.com/trackasia/trackasia/blob/main/PROJECT_TIERS.md#project-tiers).

## Renderer Modularization & Metal

![image-metal](https://user-images.githubusercontent.com/53421382/214308933-66cd4efb-b5a5-4de3-b4b4-7ed59045a1c3.png)

TrackAsia GL Native is being actively developed. Our big goal for 2023 is to modularize the OpenGL renderer and implement a Metal graphics backend (https://developer.apple.com/metal/). This will improve the performance and yield lower power consumption on iOS devices. At the same time, the Metal preparations will help us in the implementation of a Vulkan graphics backend.

Your help in preparing the codebase for the latest graphics backends is more than welcome. Feel free to reach out if you are interested in joining the effort!

- Check out the [news](https://track-asia.com/news/) on TrackAsia's website.
- See the [Design Proposals](https://github.com/louwers/trackasia-gl-native/tree/main/design-proposals) that have been accepted and are being worked on, the most recent ones being the [Rendering Modularization Design Proposal](design-proposals/2022-10-27-rendering-modularization.md) and the [Metal Port Design Proposal](design-proposals/2022-11-29-metal-port.md).

## Contributing

To contribute to TrackAsia GL Native, see [`CONTRIBUTING.md`](CONTRIBUTING.md) and (if applicable) the specific instructions for the platform you want to contribute to.

### Getting Involved

Join the `#trackasia-native` Slack channel at OSMUS. Get an invite at https://slack.openstreetmap.us/ 


## Sponsors

We thank everyone who supported us financially in the past and special thanks to the people and organizations who support us with recurring donations!

Read more about the TrackAsia Sponsorship Program at [https://track-asia.com/sponsors/](https://track-asia.com/sponsors/).

Platinum:

<img src="https://track-asia.com/img/aws-logo.svg" alt="Logo AWS" width="25%"/>


Silver:

<img src="https://track-asia.com/img/meta-logo.svg" alt="Logo Meta" width="50%"/>

Stone:

[MIERUNE Inc.](https://www.mierune.co.jp/?lang=en)

Backers and Supporters:

[![](https://opencollective.com/trackasia/backers.svg?avatarHeight=50&width=600)](https://opencollective.com/trackasia)

## License

**TrackAsia GL Native** is licensed under the [BSD 2-Clause License](./LICENSE.md).
