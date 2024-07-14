package org.trackasia.android.maps

import android.graphics.Color
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.trackasia.android.annotations.BaseMarkerOptions
import org.trackasia.android.annotations.Marker
import org.trackasia.android.annotations.MarkerOptions
import org.trackasia.android.annotations.PolygonOptions
import org.trackasia.android.annotations.PolylineOptions
import org.trackasia.android.exceptions.InvalidMarkerPositionException
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.TrackAsiaMap.InfoWindowAdapter
import org.trackasia.android.testapp.R
import org.trackasia.android.testapp.activity.EspressoTest

// J2K: IDE suggest "Add 'fun' modifier

/**
 * This test is responsible for testing the public API.
 *
 *
 * Methods executed on MapboxMap are called from a ViewAction to ensure correct synchronisation
 * with the application UI-thread.
 *
 */
@Deprecated("remove this file when removing deprecated annotations")
class TrackAsiaMapTest : EspressoTest() {
    //
    // InfoWindow
    //
    @Test
    fun testConcurrentInfoWindowEnabled() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            trackasiaMap.isAllowConcurrentMultipleOpenInfoWindows = true
            assertTrue("ConcurrentWindows should be true", trackasiaMap.isAllowConcurrentMultipleOpenInfoWindows)
        })
    }

    @Test
    fun testConcurrentInfoWindowDisabled() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            trackasiaMap.isAllowConcurrentMultipleOpenInfoWindows = false
            TestCase.assertFalse("ConcurrentWindows should be false", trackasiaMap.isAllowConcurrentMultipleOpenInfoWindows)
        })
    }

    @Test
    fun testInfoWindowAdapter() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val infoWindowAdapter = InfoWindowAdapter { marker: Marker? -> null }
            trackasiaMap.infoWindowAdapter = infoWindowAdapter
            assertEquals("InfoWindowAdpter should be the same", infoWindowAdapter, trackasiaMap.infoWindowAdapter)
        })
    }

    //
    // Annotations
    //
    @Test
    fun testAddMarker() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val marker = trackasiaMap.addMarker(markerOptions)
            assertTrue("Marker should be contained", trackasiaMap.markers.contains(marker))
        })
    }

    @Test(expected = InvalidMarkerPositionException::class)
    fun testAddMarkerInvalidPosition() {
        MarkerOptions().marker
    }

    @Test
    fun testAddMarkers() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerList: MutableList<BaseMarkerOptions<*, *>> = ArrayList()
            val markerOptions1 = MarkerOptions().position(LatLng()).title("a")
            val markerOptions2 = MarkerOptions().position(LatLng()).title("b")
            markerList.add(markerOptions1)
            markerList.add(markerOptions2)
            val markers = trackasiaMap.addMarkers(markerList)
            assertEquals("Markers size should be 2", 2, trackasiaMap.markers.size.toLong())
            assertTrue(trackasiaMap.markers.contains(markers[0]))
            assertTrue(trackasiaMap.markers.contains(markers[1]))
        })
    }

    @Test
    fun testAddMarkersEmpty() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerList: List<BaseMarkerOptions<*, *>> = ArrayList()
            trackasiaMap.addMarkers(markerList)
            assertEquals("Markers size should be 0", 0, trackasiaMap.markers.size.toLong())
        })
    }

    @Test
    fun testAddMarkersSingleMarker() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerList: MutableList<BaseMarkerOptions<*, *>> = ArrayList()
            val markerOptions = MarkerOptions().title("a").position(LatLng())
            markerList.add(markerOptions)
            val markers = trackasiaMap.addMarkers(markerList)
            assertEquals("Markers size should be 1", 1, trackasiaMap.markers.size.toLong())
            assertTrue(trackasiaMap.markers.contains(markers[0]))
        })
    }

    @Test
    fun testAddPolygon() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polygonOptions = PolygonOptions().add(LatLng())
            val polygon = trackasiaMap.addPolygon(polygonOptions)
            assertTrue("Polygon should be contained", trackasiaMap.polygons.contains(polygon))
        })
    }

    @Test
    fun testAddEmptyPolygon() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polygonOptions = PolygonOptions()
            val polygon = trackasiaMap.addPolygon(polygonOptions)
            assertTrue("Polygon should be ignored", !trackasiaMap.polygons.contains(polygon))
        })
    }

    @Test
    fun testAddPolygons() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polygonList: MutableList<PolygonOptions> = ArrayList()
            val polygonOptions1 = PolygonOptions().fillColor(Color.BLACK).add(LatLng())
            val polygonOptions2 = PolygonOptions().fillColor(Color.WHITE).add(LatLng())
            val polygonOptions3 = PolygonOptions()
            polygonList.add(polygonOptions1)
            polygonList.add(polygonOptions2)
            polygonList.add(polygonOptions3)
            trackasiaMap.addPolygons(polygonList)
            assertEquals("Polygons size should be 2", 2, trackasiaMap.polygons.size.toLong())
            assertTrue(trackasiaMap.polygons.contains(polygonOptions1.polygon))
            assertTrue(trackasiaMap.polygons.contains(polygonOptions2.polygon))
            assertTrue("Polygon should be ignored", !trackasiaMap.polygons.contains(polygonOptions3.polygon))
        })
    }

    @Test
    fun addPolygonsEmpty() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            trackasiaMap.addPolygons(ArrayList())
            assertEquals("Polygons size should be 0", 0, trackasiaMap.polygons.size.toLong())
        })
    }

    @Test
    fun addPolygonsSingle() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polygonList: MutableList<PolygonOptions> = ArrayList()
            val polygonOptions = PolygonOptions().fillColor(Color.BLACK).add(LatLng())
            polygonList.add(polygonOptions)
            trackasiaMap.addPolygons(polygonList)
            assertEquals("Polygons size should be 1", 1, trackasiaMap.polygons.size.toLong())
            assertTrue(trackasiaMap.polygons.contains(polygonOptions.polygon))
        })
    }

    @Test
    fun testAddPolyline() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polylineOptions = PolylineOptions().add(LatLng())
            val polyline = trackasiaMap.addPolyline(polylineOptions)
            assertTrue("Polyline should be contained", trackasiaMap.polylines.contains(polyline))
        })
    }

    @Test
    fun testAddEmptyPolyline() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polylineOptions = PolylineOptions()
            val polyline = trackasiaMap.addPolyline(polylineOptions)
            assertTrue("Polyline should be ignored", !trackasiaMap.polylines.contains(polyline))
        })
    }

    @Test
    fun testAddPolylines() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polylineList: MutableList<PolylineOptions> = ArrayList()
            val polygonOptions1 = PolylineOptions().color(Color.BLACK).add(LatLng())
            val polygonOptions2 = PolylineOptions().color(Color.WHITE).add(LatLng())
            val polygonOptions3 = PolylineOptions()
            polylineList.add(polygonOptions1)
            polylineList.add(polygonOptions2)
            polylineList.add(polygonOptions3)
            trackasiaMap.addPolylines(polylineList)
            assertEquals("Polygons size should be 2", 2, trackasiaMap.polylines.size.toLong())
            assertTrue(trackasiaMap.polylines.contains(polygonOptions1.polyline))
            assertTrue(trackasiaMap.polylines.contains(polygonOptions2.polyline))
            assertTrue(
                    "Polyline should be ignored", !trackasiaMap.polylines.contains(polygonOptions3.polyline)
            )
        })
    }

    @Test
    fun testAddPolylinesEmpty() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            trackasiaMap.addPolylines(ArrayList())
            assertEquals("Polygons size should be 0", 0, trackasiaMap.polylines.size.toLong())
        })
    }

    @Test
    fun testAddPolylinesSingle() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polylineList: MutableList<PolylineOptions> = ArrayList()
            val polygonOptions = PolylineOptions().color(Color.BLACK).add(LatLng())
            polylineList.add(polygonOptions)
            trackasiaMap.addPolylines(polylineList)
            assertEquals("Polygons size should be 1", 1, trackasiaMap.polylines.size.toLong())
            assertTrue(trackasiaMap.polylines.contains(polygonOptions.polyline))
        })
    }

    @Test
    fun testRemoveMarker() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val marker = trackasiaMap.addMarker(markerOptions)
            trackasiaMap.removeMarker(marker)
            assertTrue("Markers should be empty", trackasiaMap.markers.isEmpty())
        })
    }

    @Test
    fun testRemovePolygon() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polygonOptions = PolygonOptions()
            val polygon = trackasiaMap.addPolygon(polygonOptions)
            trackasiaMap.removePolygon(polygon)
            assertTrue("Polygons should be empty", trackasiaMap.polylines.isEmpty())
        })
    }

    @Test
    fun testRemovePolyline() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val polylineOptions = PolylineOptions()
            val polyline = trackasiaMap.addPolyline(polylineOptions)
            trackasiaMap.removePolyline(polyline)
            assertTrue("Polylines should be empty", trackasiaMap.polylines.isEmpty())
        })
    }

    @Test
    fun testRemoveAnnotation() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val marker = trackasiaMap.addMarker(markerOptions)
            trackasiaMap.removeAnnotation(marker)
            assertTrue("Annotations should be empty", trackasiaMap.annotations.isEmpty())
        })
    }

    @Test
    fun testRemoveAnnotationById() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            trackasiaMap.addMarker(markerOptions)
            // id will always be 0 in unit tests
            trackasiaMap.removeAnnotation(0)
            assertTrue("Annotations should be empty", trackasiaMap.annotations.isEmpty())
        })
    }

    @Test
    fun testRemoveAnnotations() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerList: MutableList<BaseMarkerOptions<*, *>> = ArrayList()
            val markerOptions1 = MarkerOptions().title("a").position(LatLng())
            val markerOptions2 = MarkerOptions().title("b").position(LatLng())
            markerList.add(markerOptions1)
            markerList.add(markerOptions2)
            trackasiaMap.addMarkers(markerList)
            trackasiaMap.removeAnnotations()
            assertTrue("Annotations should be empty", trackasiaMap.annotations.isEmpty())
        })
    }

    @Test
    fun testClear() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerList: MutableList<BaseMarkerOptions<*, *>> = ArrayList()
            val markerOptions1 = MarkerOptions().title("a").position(LatLng())
            val markerOptions2 = MarkerOptions().title("b").position(LatLng())
            markerList.add(markerOptions1)
            markerList.add(markerOptions2)
            trackasiaMap.addMarkers(markerList)
            trackasiaMap.clear()
            assertTrue("Annotations should be empty", trackasiaMap.annotations.isEmpty())
        })
    }

    @Test
    fun testRemoveAnnotationsByList() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerList: MutableList<BaseMarkerOptions<*, *>> = ArrayList()
            val markerOptions1 = MarkerOptions().title("a").position(LatLng())
            val markerOptions2 = MarkerOptions().title("b").position(LatLng())
            markerList.add(markerOptions1)
            markerList.add(markerOptions2)
            val markers = trackasiaMap.addMarkers(markerList)
            val marker = trackasiaMap.addMarker(MarkerOptions().position(LatLng()).title("c"))
            trackasiaMap.removeAnnotations(markers)
            assertTrue("Annotations should not be empty", trackasiaMap.annotations.size == 1)
            assertTrue("Marker should be contained", trackasiaMap.annotations.contains(marker))
        })
    }

    @Test
    fun testGetAnnotationById() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val initialMarker = trackasiaMap.addMarker(markerOptions)
            val retrievedMarker = trackasiaMap.getAnnotation(0) as Marker?
            assertEquals("Markers should match", initialMarker, retrievedMarker)
        })
    }

    @Test
    fun testGetAnnotations() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(
                TrackAsiaMapAction { uiController: UiController?, view: View? -> TestCase.assertNotNull("Annotations should be non null", trackasiaMap.annotations) }
        )
    }

    @Test
    fun testGetMarkers() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(
                TrackAsiaMapAction { uiController: UiController?, view: View? -> TestCase.assertNotNull("Markers should be non null", trackasiaMap.markers) }
        )
    }

    @Test
    fun testGetPolygons() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? -> TestCase.assertNotNull("Polygons should be non null", trackasiaMap.polygons) }
        )
    }

    @Test
    fun testGetPolylines() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? -> TestCase.assertNotNull("Polylines should be non null", trackasiaMap.polylines) }
        )
    }

    @Test
    fun testGetSelectedMarkers() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? -> TestCase.assertNotNull("Selected markers should be non null", trackasiaMap.selectedMarkers) }
        )
    }

    @Test
    fun testSelectMarker() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val marker = trackasiaMap.addMarker(markerOptions)
            trackasiaMap.selectMarker(marker)
            assertTrue("Marker should be contained", trackasiaMap.selectedMarkers.contains(marker))
        })
    }

    @Test
    fun testDeselectMarker() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val marker = trackasiaMap.addMarker(markerOptions)
            trackasiaMap.selectMarker(marker)
            trackasiaMap.deselectMarker(marker)
            assertTrue("Selected markers should be empty", trackasiaMap.selectedMarkers.isEmpty())
        })
    }

    @Test
    fun testDeselectMarkers() {
        validateTestSetup()
        onView(ViewMatchers.withId(R.id.mapView)).perform(TrackAsiaMapAction { uiController: UiController?, view: View? ->
            val markerOptions = MarkerOptions().position(LatLng())
            val marker1 = trackasiaMap.addMarker(markerOptions)
            val marker2 = trackasiaMap.addMarker(markerOptions)
            trackasiaMap.selectMarker(marker1)
            trackasiaMap.selectMarker(marker2)
            trackasiaMap.deselectMarkers()
            assertTrue("Selected markers should be empty", trackasiaMap.selectedMarkers.isEmpty())
        })
    }

    inner class TrackAsiaMapAction internal constructor(private val invokeViewAction: InvokeViewAction) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isDisplayed()
        }

        override fun getDescription(): String {
            return javaClass.simpleName
        }

        override fun perform(uiController: UiController, view: View) {
            invokeViewAction.onViewAction(uiController, view)
        }
    }

    internal fun interface InvokeViewAction {
        fun onViewAction(uiController: UiController?, view: View?)
    }
}