# TrackAsia GL Native for Android

[![GitHub Action build status](https://github.com/track-asia/trackasia-gl-native/workflows/android-ci/badge.svg)](https://github.com/track-asia/trackasia-gl-native/actions/workflows/android-ci.yml) [![GitHub Action build status](https://github.com/track-asia/trackasia-gl-native/workflows/android-release/badge.svg)](https://github.com/track-asia/trackasia-gl-native/actions/workflows/android-release.yml)

TrackAsia GL Native for Android is a library for embedding interactive map views with scalable, customizable vector maps onto Android devices.

## Getting Started

1. Add bintray Maven repositories to your `build.gradle` at project level so that you can access TrackAsia packages for Android:

    ```gradle
        allprojects {
            repositories {
                ...
                mavenCentral()                
            }
        }
    ```

    > **Note**
    > 
    > [Bintray was turn off May 1st, 2021](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/) so we migrated all packages to Maven central.

2. Add the library as a dependency into your module build.gradle

    ```gradle
        dependencies {
            ...
            implementation 'io.github.track-asia:android-sdk:<version>'
            ...
        }
    ```

3. Sync Gradle and rebuild your app.

> **Note**
> TrackAsia by default ships with the proprietary Google Play Location Services. If you want to avoid pulling proprietary dependencies into your project, you can exclude Google Play Location Services as follows:
> ```gradle
>    implementation ('io.github.track-asia:android-sdk:<version>') {
>        exclude group: 'com.google.android.gms'
>    }
> ```

## Documentation

Visit [https://track-asia.com/trackasia-gl-native/android/api/](https://track-asia.com/trackasia-gl-native/android/api/) to view the current API reference Javadoc files for TrackAsia GL Native for Android.

## Contributing

See [`DEVELOPING.md`](./DEVELOPING.md) for instructions on how to get started working on the codebase.
