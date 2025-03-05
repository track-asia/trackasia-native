package com.trackasia.android.annotations

import android.graphics.PointF
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.MapView
import com.trackasia.android.maps.TrackAsiaMap
import com.trackasia.android.maps.Projection
import org.junit.Assert
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito

class InfoWindowTest {
    @InjectMocks
    var mMapView = Mockito.mock(MapView::class.java)

    @InjectMocks
    var mTrackAsiaMap = Mockito.mock(TrackAsiaMap::class.java)

    @Test
    fun testSanity() {
        val infoWindow = InfoWindow(mMapView, mTrackAsiaMap)
        Assert.assertNotNull("infoWindow should exist", infoWindow)
    }

    @Test
    fun testBoundMarker() {
        val markerOptions = MarkerOptions()
        val marker = markerOptions.position(LatLng()).marker
        val infoWindow = InfoWindow(mMapView, mTrackAsiaMap).setBoundMarker(marker)
        Assert.assertEquals("marker should match", marker, infoWindow.boundMarker)
    }

    @Test
    fun testClose() {
        val infoWindow = InfoWindow(mMapView, mTrackAsiaMap)
        infoWindow.close()
        Assert.assertEquals("infowindow should not be visible", false, infoWindow.isVisible)
    }

    @Test
    fun testOpen() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mTrackAsiaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mTrackAsiaMap)
        infoWindow.open(mMapView, MarkerOptions().position(LatLng()).marker, latLng, 0, 0)
        Assert.assertEquals("infowindow should not be visible", true, infoWindow.isVisible)
    }

    @Test
    fun testOpenClose() {
        val latLng = LatLng(0.0, 0.0)
        val projection = Mockito.mock(
            Projection::class.java
        )
        Mockito.`when`(mTrackAsiaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mTrackAsiaMap)
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
        Mockito.`when`(mTrackAsiaMap.projection).thenReturn(projection)
        Mockito.`when`(projection.toScreenLocation(latLng)).thenReturn(PointF(0f, 0f))
        val infoWindow = InfoWindow(mMapView, mTrackAsiaMap)
        infoWindow.open(mMapView, MarkerOptions().position(latLng).marker, latLng, 0, 0)
        infoWindow.update()
    }
}
