package com.trackasia.android.maps

import androidx.collection.LongSparseArray
import com.trackasia.android.annotations.Annotation
import com.trackasia.android.annotations.BaseMarkerOptions
import com.trackasia.android.annotations.Marker
import com.trackasia.android.annotations.MarkerOptions
import com.trackasia.android.annotations.PolygonOptions
import com.trackasia.android.annotations.PolylineOptions
import com.trackasia.android.geometry.LatLng
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class AnnotationManagerTest {
    private val aNativeMapView: NativeMap = Mockito.mock(NativeMapView::class.java)
    private val aMapView = Mockito.mock(MapView::class.java)
    private val annotationsArray = LongSparseArray<Annotation>()
    private val aIconManager =
        Mockito.mock(
            IconManager::class.java,
        )
    private val aTrackAsiaMap = Mockito.mock(TrackAsiaMap::class.java)

    private val annotations: Annotations = AnnotationContainer(aNativeMapView, annotationsArray)
    private val markers: Markers = MarkerContainer(aNativeMapView, annotationsArray, aIconManager)
    private val polygons: Polygons = PolygonContainer(aNativeMapView, annotationsArray)
    private val polylines: Polylines = PolylineContainer(aNativeMapView, annotationsArray)
    private val shapeAnnotations: ShapeAnnotations =
        ShapeAnnotationContainer(aNativeMapView, annotationsArray)

    private val annotationManager =
        AnnotationManager(
            aMapView,
            annotationsArray,
            aIconManager,
            annotations,
            markers,
            polygons,
            polylines,
            shapeAnnotations,
        )

    @Test
    @Throws(Exception::class)
    fun checksAddAMarker() {
        val aMarker =
            Mockito.mock(
                Marker::class.java,
            )
        val aId = 5L
        Mockito.`when`(aNativeMapView.addMarker(aMarker)).thenReturn(aId)
        val aMarkerOptions =
            Mockito.mock(
                BaseMarkerOptions::class.java,
            )
        val aTrackAsiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito.`when`(aMarkerOptions.marker).thenReturn(aMarker)
        annotationManager.addMarker(aMarkerOptions, aTrackAsiaMap)
        Assert.assertEquals(aMarker, annotationManager.annotations[0])
        Assert.assertEquals(aMarker, annotationManager.getAnnotation(aId))
    }

    @Test
    @Throws(Exception::class)
    fun checksAddMarkers() {
        val firstId = 1L
        val secondId = 2L
        val markerList: MutableList<BaseMarkerOptions<*, *>> = ArrayList()
        val firstMarkerOption = MarkerOptions().position(LatLng()).title("first")
        val secondMarkerOption = MarkerOptions().position(LatLng()).title("second")
        markerList.add(firstMarkerOption)
        markerList.add(secondMarkerOption)
        val aTrackAsiaMap = Mockito.mock(TrackAsiaMap::class.java)
        Mockito
            .`when`(
                aNativeMapView.addMarker(
                    ArgumentMatchers.any(
                        Marker::class.java,
                    ),
                ),
            ).thenReturn(firstId, secondId)
        Mockito
            .`when`(aNativeMapView.addMarkers(ArgumentMatchers.anyList()))
            .thenReturn(longArrayOf(firstId, secondId))
        annotationManager.addMarkers(markerList, aTrackAsiaMap)
        Assert.assertEquals(2, annotationManager.annotations.size)
        Assert.assertEquals("first", (annotationManager.annotations[0] as Marker).title)
        Assert.assertEquals("second", (annotationManager.annotations[1] as Marker).title)
        Assert.assertEquals("first", (annotationManager.getAnnotation(firstId) as Marker).title)
        Assert.assertEquals("second", (annotationManager.getAnnotation(secondId) as Marker).title)
    }

    @Test
    @Throws(Exception::class)
    fun checksAddEmptyPolygon() {
        val pId = 5L
        val polygonOptions = PolygonOptions()
        val polygon = polygonOptions.polygon
        Mockito.`when`(aNativeMapView.addPolygon(polygon)).thenReturn(pId)
        val resultPolygon = annotationManager.addPolygon(polygonOptions, aTrackAsiaMap)
        Assert.assertEquals(polygon, resultPolygon)
        Assert.assertEquals(pId, resultPolygon.id)
        Assert.assertEquals(1, annotationManager.annotations.size)
        Assert.assertEquals(polygon, annotationManager.annotations[0])
    }

    @Test
    @Throws(Exception::class)
    fun checksAddEmptyPolyline() {
        val pId = 5L
        val polylineOptions = PolylineOptions()
        val polyline = polylineOptions.polyline
        Mockito.`when`(aNativeMapView.addPolyline(polyline)).thenReturn(pId)
        val resultPolyline = annotationManager.addPolyline(polylineOptions, aTrackAsiaMap)
        Assert.assertEquals(polyline, resultPolyline)
        Assert.assertEquals(pId, resultPolyline.id)
        Assert.assertEquals(1, annotationManager.annotations.size)
        Assert.assertEquals(polyline, annotationManager.annotations[0])
    }
}
