// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "TrackAsia Native",
    products: [
        .library(
            name: "Mapbox",
            targets: ["Mapbox"]
        ),
    ],
    dependencies: [
    ],
    targets: [
        .binaryTarget(
            name: "Mapbox",
            url: "TRACKASIA_PACKAGE_URL",
            checksum: "TRACKASIA_PACKAGE_CHECKSUM"
        ),
    ]
)
