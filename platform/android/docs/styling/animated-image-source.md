# Animated Image Source

{{ activity_source_note("AnimatedImageSourceActivity.kt") }}

In this example we will see how we can animate an image source. This is the TrackAsia Native equivalent of [this TrackAsia GL JS example](https://trackasia.com/trackasia-gl-js/docs/examples/animate-images/).

<figure markdown="span">
  <video controls width="400" poster="{{ s3_url("animated_image_source_thumbnail.jpg") }}" >
    <source src="{{ s3_url("animated_image_source.mp4") }}" />
  </video>
  {{ openmaptiles_caption() }}
</figure>

We set up an [image source](https://trackasia.com/trackasia-style-spec/sources/#image) in a particular quad. Then we kick of a runnable that periodically updates the image source.

```kotlin title="Creating the image source"
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/AnimatedImageSourceActivity.kt:onMapReady"
```

```kotlin title="Updating the image source"
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/AnimatedImageSourceActivity.kt:setImage"
```
