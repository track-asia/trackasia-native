# Draggable Marker

{{ activity_source_note("DraggableMarkerActivity.kt") }}

<figure markdown="span">
  <video controls width="400" poster="{{ s3_url("draggable_marker_thumbnail.jpg") }}" >
    <source src="{{ s3_url("draggable_marker.mp4") }}" />
  </video>
</figure>

## Adding a marker on tap

```kotlin title="Adding a tap listener to the map to add a marker on tap"
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DraggableMarkerActivity.kt:addOnMapClickListener"
```

## Allowing markers to be dragged

This is slightly more involved, as we implement it by implementing a `DraggableSymbolsManager` helper class.

This class is initialized and we pass a few callbacks when when markers are start or end being dragged.

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DraggableMarkerActivity.kt:draggableSymbolsManager"
```

The implementation of `DraggableSymbolsManager` follows. In its initializer we define a handler for when a user long taps on a marker. This then starts dragging that marker. It does this by temporarily suspending all other gestures.

We create a custom implementation of `MoveGestureDetector.OnMoveGestureListener` and pass this to an instance of `AndroidGesturesManager` linked to the map view.

!!! tip
      See [trackasia-gestures-android](https://github.com/trackasia/trackasia-gestures-android) for the implementation details of the gestures library used by TrackAsia Android.

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DraggableMarkerActivity.kt:DraggableSymbolsManager"
```
