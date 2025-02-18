[![TrackAsia Logo](https://trackasia.org/img/trackasia-logo-big.svg)](https://trackasia.org/)

# TrackAsia Native

[![codecov](https://codecov.io/github/trackasia/trackasia-native/branch/main/graph/badge.svg?token=8ZQRRY56ZA)](https://codecov.io/github/trackasia/trackasia-native) [![](https://img.shields.io/badge/Slack-%23trackasia--native-2EB67D?logo=slack)](https://slack.openstreetmap.us/)

TrackAsia Native is a free and open-source library for publishing maps in your apps and desktop applications on various platforms. Fast displaying of maps is possible thanks to GPU-accelerated vector tile rendering.

This project originated as a fork of Mapbox GL Native, before their switch to a non-OSS license in December 2020. For more information, see: [`FORK.md`](./FORK.md).

<p align="center">
  <img src="https://user-images.githubusercontent.com/649392/211550776-8779041a-7c12-4bed-a7bd-c2ec80af2b29.png" alt="Android device with TrackAsia" width="24%">   <img src="https://user-images.githubusercontent.com/649392/211550762-0f42ebc9-05ab-4d89-bd59-c306453ea9af.png" alt="iOS device with TrackAsia" width="25%">
</p>

## Getting Started

## Android

Add [the latest version](https://central.sonatype.com/artifact/io.github.track-asia/android-sdk/versions) of TrackAsia Native Android as a dependency to your project.

```gradle
    dependencies {
        ...
        implementation 'io.github.track-asia:android-sdk:11.5.1'
        ...
    }
```

Add a `MapView` to your layout XML file:

```xml
<com.trackasia.android.maps.MapView
    android:id="@+id/mapView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    />
```

> [!TIP]
> There are external projects such as [Ramani Maps](https://github.com/ramani-maps/ramani-maps) and [TrackAsia Compose Playground](https://github.com/Rallista/trackasia-compose-playground) available to intergrate TrackAsia Native Android with Compose-based projects.

Next, initialize the map in an activity:

<details><summary>Show code</summary>

```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.trackasia.android.Trackasia
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.testapp.R

class MainActivity : AppCompatActivity() {

    // Declare a variable for MapView
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init TrackAsia
        TrackAsia.getInstance(this)

        // Init layout view
        val inflater = LayoutInflater.from(this)
        val rootView = inflater.inflate(R.layout.activity_main, null)
        setContentView(rootView)

        // Init the MapView
        mapView = rootView.findViewById(R.id.mapView)
        mapView.getMapAsync { map ->
            map.setStyle("https://maps.track-asia.com/styles/v1/streets.json?key=public_key")
            map.cameraPosition = CameraPosition.Builder().target(LatLng(0.0,0.0)).zoom(1.0).build()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
```
</details>

For more information, refer to the [Android API Documentation](https://trackasia.com/trackasia-native/android/api/), [Android Examples Documentation](https://trackasia.com/trackasia-native/android/examples/getting-started/) or the [TrackAsia Native Android `README.md`](platform/android/README.md).

## iOS

You can find TrackAsia Native iOS on [Cocoapods](https://cocoapods.org/) and on the [Swift Package Index](https://swiftpackageindex.com/trackasia/trackasia-gl-native-distribution). You can also TrackAsia Native iOS [as a dependency to Xcode directly](https://trackasia.com/trackasia-native/ios/latest/documentation/trackasia-native-for-ios/gettingstarted/#Add-TrackAsia-Native-as-a-dependency).

TrackAsia Native iOS uses UIKit. To intergrate it with an UIKit project, you can use

```swift
class SimpleMap: UIViewController, MLNMapViewDelegate {
    var mapView: MLNMapView!

    override func viewDidLoad() {
        super.viewDidLoad()
        mapView = MLNMapView(frame: view.bounds)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        view.addSubview(mapView)
        mapView.delegate = self
    }

    func mapView(_: MLNMapView, didFinishLoading _: MLNStyle) {
    }
}
```

You need to create a wrapper when using SwiftUI.

```swift
import TrackAsia

struct SimpleMap: UIViewRepresentable {
    func makeUIView(context _: Context) -> MLNMapView {
        let mapView = MLNMapView()
        return mapView
    }

    func updateUIView(_: MLNMapView, context _: Context) {}
}
```

You can also use [TrackAsiaSwiftUI](https://github.com/trackasia/swiftui-dsl), a wrapper around TrackAsia Native iOS that offers a declarative API like SwiftUI.

The [iOS Documentation](https://trackasia.com/trackasia-native/ios/latest/documentation/trackasia/) contains many examples and the entire API of the library. You might also want to check out the [TrackAsia Native iOS `README.md`](platform/ios/README.md).

## Node.js

There is an [npm package](https://www.npmjs.com/package/@trackasia/trackasia-gl-native) for using TrackAsia Native in a Node.js project. The source code of this project [can be found in this repository](https://github.com/trackasia/trackasia-native/tree/main/platform/node).

## Qt

Please check out the [`trackasia/trackasia-native-qt` repository](https://github.com/trackasia/trackasia-native-qt) to learn how to intergrate TrackAsia Native with a Qt project.

## Other Platforms

TrackAsia Native can also be built on [Linux](platform/linux/README.md), [Windows](platform/windows/README.md) and [macOS](platform/macos/README.md).

## Contributing

> [!NOTE]
> This section is only relevant for people who want to contribute to TrackAsia Native.

TrackAsia Native has at its core a C++ library. This is where the bulk of development is currently happening.

To get started with the code base, you need to clone the the repository including all its submodules.

All contributors use pull requests from a private fork. [Fork the project](https://github.com/trackasia/trackasia-native/fork). Then run:

```bash
git clone --recurse-submodules git@github.com:<YOUR NAME>/trackasia-native.git
git remote add origin https://github.com/trackasia/trackasia-native.git
```

Check out issues labelled as a [good first issue](https://github.com/trackasia/trackasia-native/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22).

## Core

- [`CONTRIBUTING.md`](CONTRIBUTING.md)
- [TrackAsia Native Markdown Book](https://trackasia.com/trackasia-native/docs/book/design/ten-thousand-foot-view.html): architectural notes
- [GitHub Wiki](https://github.com/trackasia/trackasia-native/wiki): low-friction way to share information with the community
- [Core C++ API Documentation](https://trackasia.com/trackasia-native/cpp/api/) (unstable)

## Android

Open `platform/android` with Android Studio.

More information: [`platform/android/DEVELOPING.md`](platform/android/DEVELOPING.md).

## iOS

You need to use [Bazel](https://bazel.build/) to generate an Xcode project. Install [`bazelisk`](https://formulae.brew.sh/formula/bazelisk) (a wrapper that installs the required Bazel version). Next, use:

```bash
bazel run //platform/ios:xcodeproj --@rules_xcodeproj//xcodeproj:extra_common_flags="--//:renderer=metal"
xed platform/ios/TrackAsia.xcodeproj
```

To generate and open the Xcode project.

More information: [`platform/ios/CONTRIBUTING.md`](platform/ios/CONTRIBUTING.md).

## Other Platforms

See [`/platform`](/platform) and navigate to the platform you are interested in for more information.

## Getting Involved

Join the `#trackasia-native` Slack channel at OSMUS. Get an invite at https://slack.openstreetmap.us/

### Bounties 💰

Thanks to our sponsors, we are able to award bounties to developers making contributions toward certain [bounty directions](https://github.com/trackasia/trackasia/issues?q=is%3Aissue+is%3Aopen+label%3A%22bounty+direction%22). To get started doing bounties, refer to the [step-by-step bounties guide](https://trackasia.org/roadmap/step-by-step-bounties-guide/).

We thank everyone who supported us financially in the past and special thanks to the people and organizations who support us with recurring donations!

Read more about the TrackAsia Sponsorship Program at [https://trackasia.org/sponsors/](https://trackasia.org/sponsors/).

Gold:

<a href="https://aws.amazon.com/location"><img src="https://trackasia.org/img/aws-logo.svg" alt="Logo AWS" width="25%"/></a>

<a href="https://meta.com"><img src="https://trackasia.org/img/meta-logo.svg" alt="Logo Meta" width="25%"/></a>

Silver:

<a href="https://www.mierune.co.jp/?lang=en"><img src="https://trackasia.org/img/mierune-logo.svg" alt="Logo MIERUNE" width="25%"/></a>

<a href="https://komoot.com/"><img src="https://trackasia.org/img/komoot-logo.svg" alt="Logo komoot" width="25%"/></a>

<a href="https://www.jawg.io/"><img src="https://trackasia.org/img/jawgmaps-logo.svg" alt="Logo JawgMaps" width="25%"/></a>

<a href="https://www.radar.com/"><img src="https://trackasia.org/img/radar-logo.svg" alt="Logo Radar" width="25%"/></a>

<a href="https://www.microsoft.com/"><img src="https://trackasia.org/img/msft-logo.svg" alt="Logo Microsoft" width="25%"/></a>

<a href="https://www.mappedin.com/"><img src="https://trackasia.org/img/mappedin-logo.svg" alt="Logo mappedin" width="25%"/></a>

<a href="https://www.mapme.com/"><img src="https://trackasia.org/img/mapme-logo.svg" alt="Logo mapme" width="25%"/></a>

Backers and Supporters:

[![](https://opencollective.com/trackasia/backers.svg?avatarHeight=50&width=600)](https://opencollective.com/trackasia)

## License

**TrackAsia Native** is licensed under the [BSD 2-Clause License](./LICENSE.md).
