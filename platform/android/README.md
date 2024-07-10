# TrackAsia Native for Android

[![GitHub Action build status](https://github.com/track-asia/trackasia-native/workflows/android-ci/badge.svg)](https://github.com/track-asia/trackasia-native/actions/workflows/android-ci.yml) [![GitHub Action build status](https://github.com/track-asia/trackasia-native/workflows/android-release/badge.svg)](https://github.com/track-asia/trackasia-native/actions/workflows/android-release.yml)

TrackAsia Native for Android is a library for embedding interactive map views with scalable, customizable vector maps onto Android devices.

## Getting Started

Visit [https://track-asia.com/trackasia-native/docs/book/android/getting-started-guide.html](https://track-asia.com/trackasia-native/docs/book/android/getting-started-guide.html) to view the Getting Started Guide for TrackAsia Native for Android.

## Documentation

Visit [https://track-asia.com/trackasia-native/android/api/](https://track-asia.com/trackasia-native/android/api/) to view the current API reference Javadoc files for TrackAsia Native for Android.

## Contributing

See [`DEVELOPING.md`](./DEVELOPING.md) for instructions on how to get started working on the codebase.



mvn dependency:purge-local-repository
mvn deploy

md5sum android-sdk-2.0.0-sources.jar | cut -d ' ' -f 1 > android-sdk-2.0.0-sources.jar.md5
md5sum android-sdk-2.0.0.aar | cut -d ' ' -f 1 > android-sdk-2.0.0.aar.md5
md5sum android-sdk-2.0.0.module | cut -d ' ' -f 1 > android-sdk-2.0.0.module.md5
md5sum android-sdk-2.0.0.pom | cut -d ' ' -f 1 > android-sdk-2.0.0.pom.md5


sha1sum android-sdk-2.0.0-sources.jar | cut -d ' ' -f 1 > android-sdk-2.0.0-sources.jar.sha1
sha1sum android-sdk-2.0.0.aar | cut -d ' ' -f 1 > android-sdk-2.0.0.aar.sha1
sha1sum android-sdk-2.0.0.module | cut -d ' ' -f 1 > android-sdk-2.0.0.module.sha1
sha1sum android-sdk-2.0.0.pom | cut -d ' ' -f 1 > android-sdk-2.0.0.pom.sha1

