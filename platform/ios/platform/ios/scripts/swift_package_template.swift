// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "Trackasia GL Native",
    products: [
        .library(
            name: "Mapbox",
            targets: ["Mapbox"])
    ],
    dependencies: [
    ],    
    targets: [
        .binaryTarget(
            name: "Mapbox",
            url: "MAPBOX_PACKAGE_URL",
            checksum: "MAPBOX_PACKAGE_CHECKSUM")
    ]
)