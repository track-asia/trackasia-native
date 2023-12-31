package com.trackasia.android.maps

import android.graphics.PointF
import androidx.test.espresso.UiController
import com.trackasia.android.camera.CameraUpdateFactory
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.geometry.LatLngBounds
import com.trackasia.android.testapp.action.TrackasiaMapAction.invoke
import com.trackasia.android.testapp.activity.BaseTest
import com.trackasia.android.testapp.activity.espresso.PixelTestActivity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VisibleRegionTest : BaseTest() {

    override fun getActivityClass(): Class<*> {
        return PixelTestActivity::class.java
    }

    override
    fun beforeTest() {
        super.beforeTest()
        mapView = (rule.activity as PixelTestActivity).mapView
    }

    @Test
    fun visibleRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val visibleRegion = mapboxMap.projection.visibleRegion
            assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
        }
    }

    @Test
    fun paddedVisibleRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(
                mapView.width / 4,
                mapView.height / 4,
                mapView.width / 4,
                mapView.height / 4
            )

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 1)
            assertTrue(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedLeftVisibleRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(mapView.width / 4, 0, 0, 0)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedTopVisibleRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(0, mapView.height / 4, 0, 0)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f)))
        }
    }

    @Test
    fun paddedRightVisibleRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(0, 0, mapView.width / 4, 0)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)))
        }
    }

    @Test
    fun paddedBottomVisibleRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(0, 0, 0, mapView.height / 4)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat())))
        }
    }

    @Test
    fun visibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val visibleRegion = mapboxMap.projection.visibleRegion
            assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
        }
    }

    @Test
    fun paddedVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(
                mapView.width / 4,
                mapView.height / 4,
                mapView.width / 4,
                mapView.height / 4
            )

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 1)
            assertTrue(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedLeftVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(mapView.width / 4, 0, 0, 0)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f)))
        }
    }

    @Test
    fun paddedTopVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { ui: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            ui.loopMainThreadForAtLeast(5000)
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(0, mapView.height / 4, 0, 0)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f)))
        }
    }

    @Test
    fun paddedRightVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(0, 0, mapView.width / 4, 0)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)))
        }
    }

    @Test
    fun paddedBottomVisibleRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )

            mapboxMap.setPadding(0, 0, 0, mapView.height / 4)

            val visibleRegion = mapboxMap.projection.getVisibleRegion(false)
            val filtered = latLngs.filter { visibleRegion.latLngBounds.contains(it) }
            assertTrue(filtered.size == 6)
            assertFalse(filtered.contains(mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat())))
        }
    }

    @Test
    fun visibleRotatedRegionTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val d = Math.min(mapboxMap.width, mapboxMap.height) / 4
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val visibleRegion = mapboxMap.projection.visibleRegion
                assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRotatedRegionOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val d = Math.min(mapboxMap.width, mapboxMap.height) / 4
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val visibleRegion = mapboxMap.projection.visibleRegion
                assertTrue(latLngs.all { visibleRegion.latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRegionWithBoundsEqualTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            mapboxMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            val visibleRegion = mapboxMap.projection.visibleRegion
            assertTrue(latLngBounds == visibleRegion.latLngBounds)
            assertTrue(latLngs.all { latLngBounds.contains(it) })
        }
    }

    @Test
    fun visibleRegionBoundsOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(0f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, 0f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), 0f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height / 2f)
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width.toFloat(), mapView.height.toFloat())
                    .also { it.longitude += 360 },
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height.toFloat()),
                mapboxMap.getLatLngFromScreenCoords(0f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f)
            )
            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            mapboxMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            assertTrue(latLngs.all { latLngBounds.contains(it) })
        }
    }

    @Test
    fun visibleRegionBoundsOverDatelineLatitudeZeroTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 180.0), 8.0))
            val shift = mapboxMap.getLatLngFromScreenCoords(0f, 0f)
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 180.0 - shift.longitude)))

            val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            mapboxMap.projection.getVisibleCoordinateBounds(bounds)
            val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
            val visibleRegion = mapboxMap.projection.visibleRegion
            assertTrue(latLngBounds == visibleRegion.latLngBounds)
        }
    }

    @Test
    fun visibleRotatedRegionBoundEqualTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 8.0))
            val d = Math.min(mapboxMap.width, mapboxMap.height) / 4
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                mapboxMap.projection.getVisibleCoordinateBounds(bounds)
                val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
                val visibleRegion = mapboxMap.projection.visibleRegion
                assertTrue(latLngBounds == visibleRegion.latLngBounds)
                assertTrue(latLngs.all { latLngBounds.contains(it) })
            }
        }
    }

    @Test
    fun visibleRotatedRegionBoundsOverDatelineTest() {
        validateTestSetup()
        invoke(mapboxMap) { _: UiController, mapboxMap: TrackasiaMap ->
            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 179.0), 8.0))
            val d = Math.min(mapboxMap.width, mapboxMap.height) / 4
            val latLngs = listOf(
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f - d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f + d / 2f, mapView.height / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f - d / 2f),
                mapboxMap.getLatLngFromScreenCoords(mapView.width / 2f, mapView.height / 2f + d / 2f)
            )

            for (bearing in 45 until 360 step 45) {
                mapboxMap.moveCamera(CameraUpdateFactory.bearingTo(bearing.toDouble()))
                val bounds = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
                mapboxMap.projection.getVisibleCoordinateBounds(bounds)
                val latLngBounds = LatLngBounds.from(bounds[0], bounds[1], bounds[2], bounds[3])
                assertTrue(latLngs.all { latLngBounds.contains(it) })
            }
        }
    }

    private fun TrackasiaMap.getLatLngFromScreenCoords(x: Float, y: Float): LatLng {
        return this.projection.fromScreenLocation(PointF(x, y))
    }
}
