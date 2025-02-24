# User Interactions

Learn how to work with gesture recognizers

TrackAsia Native iOS provides a set of built-in gesture recognizers. You can customize or supplement these gestures according to your use case. You see what gesture recognizers are on your ``MLNMapView`` by accessing the `gestureRecognizers` property on your map.

## Configuring user interaction

Several properties on an ``MLNMapView`` provide ways to enable or disable a set of gesture recognizers. Boolean values are set to `true` by default.

- ``MLNMapView/zoomEnabled`` - Allows the user to zoom in or out by pinching two fingers, double-tapping, tapping with two fingers, or double-tapping then dragging vertically. Accepts Boolean values.
- ``MLNMapView/scrollEnabled`` - Allows the user to scroll by dragging or swiping one finger. Accepts Boolean values.
- ``MLNMapView/rotateEnabled`` - Allows the user to rotate by moving two fingers in a circular motion. Accepts Boolean values.
- ``MLNMapView/pitchEnabled`` - Allows the user to tilt the map by vertically dragging two fingers. Accepts Boolean values.
- ``MLNMapView/decelerationRate`` - Determines the rate of deceleration after the user lifts their finger. You can set the value using the  ``MLNMapViewDecelerationRateNormal``, ``MLNMapViewDecelerationRateFast``, or ``MLNMapViewDecelerationRateImmediate`` constants.

## Individual gestures

|Gesture | Description | Related Property |
|:-------:|----------------| -----------|
|Pinch    | Zooms in or out on the map's anchor point | ``MLNMapView/zoomEnabled``  |
|Rotation | Changes the ``MLNMapView`` direction based on the user rotating two fingers in a circular motion | ``MLNMapView/rotateEnabled`` |
|Single tap | Selects/deselects the annotation that you tap. | |
|Double tap | Zooms in on the map's anchor point | ``MLNMapView/zoomEnabled``  |
|Two-finger tap | Zooms out with the map's anchor point centered | ``MLNMapView/zoomEnabled``  |
|Pan | Scrolls across mapView (_note: if_ `MLNUserTrackingModeFollow` _is being used, it will be disabled once the user pans_)| ``MLNMapView/scrollEnabled`` |
|Two-finger drag | Adjusts the pitch of the ``MLNMapView`` | `pitchEnabled` |
|One-finger zoom | Tap twice; on second tap, hold your finger on the map and pan up to zoom in, or down to zoom out | ``MLNMapView/zoomEnabled`` |

@Video(
   source: "rotation.mp4",
   poster: "rotation.png",
   alt: "A short video showing the gesture for rotation with two fingers.") {
    Rotation with two fingers.
}

@Video(
   source: "quickzoom.mp4",
   poster: "quickzoom.png",
   alt: "A short video showing rotation with one finger.") {
    One finger zoom with a double tap.
}

## Adding custom gesture recognizers

You can add [`UIGestureRecognizer`s](https://developer.apple.com/documentation/uikit/uigesturerecognizer) to your map programmatically or via storyboard. Adding custom responses to gesture recognizers can enhance your user's experience, but try to use standard gestures where possible.

The gesture recognizers that you add will take priority over the built-in gesture recognizer. You can also set up your own gesture recognizer to work simultaneously with built-in gesture recognizers by using [`gestureRecognizer:shouldRecognizeSimultaneouslyWithGestureRecognizer:`](https://developer.apple.com/documentation/uikit/uigesturerecognizerdelegate/1624208-gesturerecognizer), allowing you to enhance already existing gesture recognizers.

You can also add gesture recognizers that are only called when the default gesture recognizer fails (and vice versa), such as when a user taps on a part of the map that is not an annotation. The documentation for ``MLNMapView`` includes an example of how to create a fallback gesture recognizer.

If you would like to disable a specific set of gesture recognizers, such as zoom, you can set the Boolean value for the appropriate property to `false`. You can then add your own gesture recognizers to perform those actions.
