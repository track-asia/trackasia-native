package org.trackasia.android.maps

import android.graphics.PointF
import androidx.test.espresso.UiController
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.trackasia.android.camera.CameraUpdateFactory
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.geometry.LatLngBounds
import org.trackasia.android.testapp.action.TrackAsiaMapAction.invoke
import org.trackasia.android.testapp.activity.BaseTest
import org.trackasia.android.testapp.activity.espresso.PixelTestActivity

@Ignore("https://github.com/track-asia/trackasia-native/issues/2468")
class VisibleRegionTest : BaseTest() {
    override fun getActivityClass(): Class<*> {
        return PixelTestActivity::class.java
    }

    override fun beforeTest() {
        super.beforeTest()
        mapView = (rule.activity as PixelTestActivity).mapView
    }

    @Test
    fun visibleRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )
            val visibleRegion = trackasiaMap.projection.visibleRegion
            assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
        }
    }

    @Test
    fun paddedVisibleRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(
                mapView.width / 4,
                mapView.height / 4,
                mapView.width / 4,
                mapView.height / 4,
            )

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 1)
            assertTrue(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedLeftVisibleRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(mapView.width / 4, 0, 0, 0)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedTopVisibleRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(0, mapView.height / 4, 0, 0)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f)))
        }
    }

    @Test
    fun paddedRightVisibleRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(0, 0, mapView.width / 4, 0)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)))
        }
    }

    @Test
    fun paddedBottomVisibleRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(0, 0, 0, mapView.height / 4)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat())))
        }
    }

    @Test
    fun visibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )
            val visibleRegion = trackasiaMap.projection.visibleRegion
            assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
        }
    }

    @Test
    fun paddedVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(
                mapView.width / 4,
                mapView.height / 4,
                mapView.width / 4,
                mapView.height / 4,
            )

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 1)
            assertTrue(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedLeftVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(mapView.width / 4, 0, 0, 0)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedTopVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { ui: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            ui.loopMainThreadForAtLeast(5000)
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(0, mapView.height / 4, 0, 0)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f)))
        }
    }

    @Test
    fun paddedRightVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(0, 0, mapView.width / 4, 0)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)))
        }
    }

    @Test
    fun paddedBottomVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )

            trackasiaMap.setPadding(0, 0, 0, mapView.height / 4)

            val visibleRegion = trackasiaMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat())))
        }
    }

    @Test
    fun visibleRotatedRegionTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val d = Math.min(trackasiaMap.width, trackasiaMap.height) / 4
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f),
                )

            for (bearing in 45 until 360 step 45) {
                trackasiaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val visibleRegion = trackasiaMap.projection.visibleRegion
                assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRotatedRegionOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val d = Math.min(trackasiaMap.width, trackasiaMap.height) / 4
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f),
                )

            for (bearing in 45 until 360 step 45) {
                trackasiaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val visibleRegion = trackasiaMap.projection.visibleRegion
                assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRegionWithBoundsEqualTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )
            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            trackasiaMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            val visibleRegion = trackasiaMap.projection.visibleRegion
            assertTrue(latLngBounds == visibleRegion.latLngBounds)
            assertTrue(latLngs.all { latLngBounds.contains(it) })
        }
    }

    @Test
    fun visibleRegionBoundsOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(0f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                        .also { it.longitude += 360 },
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                    trackasiaMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                )
            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            trackasiaMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            assertTrue(latLngs.all { latLngBounds.contains(it) })
        }
    }

    @Test
    fun visibleRegionBoundsOverDatelineLatitudeZeroTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val shift = trackasiaMap.getLatLngFromScreenCoords(0f, 0f)
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 180.0 - shift.longitude)))

            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            trackasiaMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            val visibleRegion = trackasiaMap.projection.visibleRegion
            assertTrue(latLngBounds == visibleRegion.latLngBounds)
        }
    }

    @Test
    fun visibleRotatedRegionBoundEqualTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val d = Math.min(trackasiaMap.width, trackasiaMap.height) / 4
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f),
                )

            for (bearing in 45 until 360 step 45) {
                trackasiaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                trackasiaMap.projection.getVisibleCoordinateBounds(bounds)
                val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
                val visibleRegion = trackasiaMap.projection.visibleRegion
                assertTrue(latLngBounds == visibleRegion.latLngBounds)
                assertTrue(latLngs.all { latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRotatedRegionBoundsOverDatelineTest() {
        validateTestSetup()
        invoke(trackasiaMap) { _: UiController, trackasiaMap: TrackAsiaMap ->
            trackasiaMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 179.0), 8.0))
            val d = Math.min(trackasiaMap.width, trackasiaMap.height) / 4
            val latLngs =
                listOf(
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                    trackasiaMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f),
                )

            for (bearing in 45 until 360 step 45) {
                trackasiaMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                trackasiaMap.projection.getVisibleCoordinateBounds(bounds)
                val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
                assertTrue(latLngs.all { latLngBounds.contains(it) })
            }
        }
    }

    private fun TrackAsiaMap.getLatLngFromScreenCoords(
        x: Float,
        y: Float,
    ): LatLng {
        return this.projection.fromScreenLocation(PointF(x, y))
    }
}
