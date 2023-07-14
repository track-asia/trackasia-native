package org.trackasia.android.annotations

import android.graphics.PointF
import org.trackasia.android.geometry.LatLng
import org.trackasia.android.maps.MapView
import org.trackasia.android.maps.trackasiaMap
import org.trackasia.android.maps.Projection
import org.junit.Assert
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito

class InfoWindowTest {
    @InjectMocks
    var mMapView = Mockito.mock(MapView::class.java)

    @InjectMocks
    var mtrackasiaMap = Mockito.mock(trackasiaMap::class.java)

    @Test
    fun testSanity() {
        val infoWindow = InfoWindow(mMapView, mtrackasiaMap)
        Assert.assertNotNull("infoWindow should exist", infoWindow)
    }

    @Test
    fun testBoundMarker() {
        val markerOptions = MarkerOptions()
        val marker = markerOptions.position(LatLng()).marker
        val infoWindow = InfoWindow(mMapView, mtrackasiaMap).setBoundMarker(marker)
        Assert.assertEquals("marker should match", marker, infoWindow.boundMarker)
    }

    @Test
    fun testClose() {
        val infoWindow = InfoWindow(mMapView, mtrackasiaMap)
        infoWindow.close()
        Assert.assertEquals("infowindow should not be visible", false, infoWindow.isVisible)
    }

    @Test
    fun testOpen() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mtrackasiaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mtrackasiaMap)
        infoWindow.open(mMapView, MarkerOptions().position(LatLng()).marker, latLng, 0, 0)
        Assert.assertEquals("infowindow should not be visible", true, infoWindow.isVisible)
    }

    @Test
    fun testOpenClose() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mtrackasiaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mtrackasiaMap)
        infoWindow.open(mMapView, MarkerOptions().position(LatLng()).marker, latLng, 0, 0)
        infoWindow.close()
        Assert.assertEquals("infowindow should not be visible", false, infoWindow.isVisible)
    }

    @Test
    fun testUpdate() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mtrackasiaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mtrackasiaMap)
        infoWindow.open(mMapView, MarkerOptions().position(latLng).marker, latLng, 0, 0)
        infoWindow.update()
    }
}
