# Data Driven Style

{{ activity_source_note("DataDrivenStyleActivity.kt") }}

In this example we will look at various types of data-driven styling.

The examples with 'Source' in the title apply data-driven styling the [parks of Amsterdam](https://github.com/trackasia/trackasia-native/blob/main/platform/android/TrackAsiaAndroidTestApp/src/main/res/raw/amsterdam.geojson). Those examples often are based on the somewhat arbitrary `stroke-width` property part of the GeoJSON features. These examples are therefore most interesting to learn about the Kotlin API that can be used for data-driven styling.

!!! tip
    Refer to the [TrackAsia Style Spec](https://trackasia.com/trackasia-style-spec/) for more information about [expressions](https://trackasia.com/trackasia-style-spec/expressions/) such as [`interpolate`](https://trackasia.com/trackasia-style-spec/expressions/#interpolate) and [`step`](https://trackasia.com/trackasia-style-spec/expressions/#step).


## Exponential Zoom Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addExponentialZoomFunction"
```

<figure markdown="span">
  <video controls width="250" poster="{{ s3_url("exponential_zoom_function_thumbnail.jpg") }}" >
    <source src="{{ s3_url("exponential_zoom_function.mp4") }}" />
  </video>
</figure>


## Interval Zoom Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addIntervalZoomFunction"
```

<figure markdown="span">
  <video controls width="250" poster="{{ s3_url("interval_zoom_function_thumbnail.jpg") }}" >
    <source src="{{ s3_url("interval_zoom_function.mp4") }}" />
  </video>
</figure>

```json title="Equivalent JSON"
["step",["zoom"],["rgba",0.0,255.0,255.0,1.0],1.0,["rgba",255.0,0.0,0.0,1.0],5.0,["rgba",0.0,0.0,255.0,1.0],10.0,["rgba",0.0,255.0,0.0,1.0]]
```

## Exponential Source Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addExponentialSourceFunction"
```

## Categorical Source Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addCategoricalSourceFunction"
```

## Identity Source Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addIdentitySourceFunction"
```

## Interval Source Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addIntervalSourceFunction"
```

## Composite Exponential Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addCompositeExponentialFunction"
```

## Identity Source Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addIdentitySourceFunction"
```

## Composite Interval Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addCompositeIntervalFunction"
```

## Composite Categorical Function

```kotlin
--8<-- "TrackAsiaAndroidTestApp/src/main/java/com/trackasia/android/testapp/activity/style/DataDrivenStyleActivity.kt:addCompositeCategoricalFunction"
```
