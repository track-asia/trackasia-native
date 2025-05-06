import TrackAsia
import SwiftUI
import UIKit

// #-example-code(SimpleMap)
struct SimpleMap: UIViewRepresentable {
    func makeUIView(context _: Context) -> MLNMapView {
        let mapView = MLNMapView()
        mapView.styleURL = URL(string: "https://maps.track-asia.com/styles/v1/streets.json?key=public_key")!
        return mapView
    }

    func updateUIView(_: MLNMapView, context _: Context) {}
}

// #-end-example-code
