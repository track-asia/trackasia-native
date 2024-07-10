# Getting Started

Setting up an Xcode project that uses TrackAsia Native for iOS.

## Create a new iOS project

Create a new (SwiftUI) iOS project with Xcode. Go to *File > New > Project...*.

## Add TrackAsia Native as a dependency

TrackAsia Native for iOS is available on [Cocoapods](https://cocoapods.org) and on the [Swift Package Index](https://swiftpackageindex.com/track-asia/trackasia-native-distribution) (for use with the Swift Package Manager). However, for this guide we will add the TrackAsia Native as a package dependency directly.

In Xcode, right click your project and select "Add Package Dependencies...":

![](AddPackageDependencies.png)

Paste the following URL and click Add Package:

```
https://github.com/track-asia/trackasia-native-distribution
```

> Note: The [trackasia-native-distributon](https://github.com/track-asia/trackasia-native-distribution) repository only exists for distributing the iOS package of TrackAsia Native. To report issues and ask questions, use the [trackasia-native](https://github.com/track-asia/trackasia-native) repository.

Verify you can import TrackAsia in your app:

```swift
import TrackAsia
```

## SwiftUI

To use TrackAsia with SwiftUI we need to create a wrapper for the UIKit view that TrackAsia provides (using UIViewRepresentable. The simplest way to implement this protocol is as follows:

<!-- include-example(SimpleMap) -->

```swift
struct SimpleMap: UIViewRepresentable {
    func makeUIView(context _: Context) -> MLNMapView {
        let mapView = MLNMapView()
        return mapView
    }

    func updateUIView(_: MLNMapView, context _: Context) {}
}
```

You can use this view directly in a SwiftUI View hierarcy, for example:

```swift
struct MyApp: App {

    var body: some Scene {
        WindowGroup {
            TrackAsiaMapView().edgesIgnoringSafeArea(.all)
        }
    }
}
```

When running your app in the simulator you should be greeted with the default [Demotiles](https://demotiles.trackasia.org/) style:

![](DemotilesScreenshot.png)

## UIKit

You can use the following `UIViewController` to get started with TrackAsia Native iOS with UIKit.

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

    // MLNMapViewDelegate method called when map has finished loading
    func mapView(_: MLNMapView, didFinishLoading _: MLNStyle) {
    }
}
```