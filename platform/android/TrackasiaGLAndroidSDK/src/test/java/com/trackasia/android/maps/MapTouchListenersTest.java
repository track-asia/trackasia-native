package com.trackasia.android.maps;

import android.graphics.PointF;

import androidx.annotation.Nullable;

import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.android.gestures.RotateGestureDetector;
import com.mapbox.android.gestures.ShoveGestureDetector;
import com.mapbox.android.gestures.StandardScaleGestureDetector;
import com.trackasia.android.geometry.LatLng;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapTouchListenersTest {

  @Nullable
  private MapGestureDetector mapGestureDetector;
  private LatLng latLng;
  private PointF pointF;

  @Before
  public void setUp() throws Exception {
    latLng = new LatLng();
    pointF = new PointF();

    Projection projection = mock(Projection.class);
    when(projection.fromScreenLocation(pointF)).thenReturn(latLng);
    mapGestureDetector = new MapGestureDetector(null,
      null, projection, null, null, null);
  }

  @Test
  public void onMapClickListenerTest() throws Exception {
    TrackasiaMap.OnMapClickListener listener = mock(TrackasiaMap.OnMapClickListener.class);
    mapGestureDetector.addOnMapClickListener(listener);
    mapGestureDetector.notifyOnMapClickListeners(pointF);
    verify(listener, times(1)).onMapClick(latLng);

    mapGestureDetector.removeOnMapClickListener(listener);
    mapGestureDetector.notifyOnMapClickListeners(pointF);
    verify(listener, times(1)).onMapClick(latLng);
  }

  @Test
  public void onMapLongClickListenerTest() throws Exception {
    TrackasiaMap.OnMapLongClickListener listener = mock(TrackasiaMap.OnMapLongClickListener.class);
    mapGestureDetector.addOnMapLongClickListener(listener);
    mapGestureDetector.notifyOnMapLongClickListeners(pointF);
    verify(listener, times(1)).onMapLongClick(latLng);

    mapGestureDetector.removeOnMapLongClickListener(listener);
    mapGestureDetector.notifyOnMapLongClickListeners(pointF);
    verify(listener, times(1)).onMapLongClick(latLng);
  }

  @Test
  public void onFlingListenerTest() throws Exception {
    TrackasiaMap.OnFlingListener listener = mock(TrackasiaMap.OnFlingListener.class);
    mapGestureDetector.addOnFlingListener(listener);
    mapGestureDetector.notifyOnFlingListeners();
    verify(listener, times(1)).onFling();

    mapGestureDetector.removeOnFlingListener(listener);
    mapGestureDetector.notifyOnFlingListeners();
    verify(listener, times(1)).onFling();
  }

  @Test
  public void onMoveListenerTest() throws Exception {
    TrackasiaMap.OnMoveListener listener = mock(TrackasiaMap.OnMoveListener.class);
    MoveGestureDetector detector = mock(MoveGestureDetector.class);
    mapGestureDetector.addOnMoveListener(listener);
    mapGestureDetector.notifyOnMoveBeginListeners(detector);
    mapGestureDetector.notifyOnMoveListeners(detector);
    mapGestureDetector.notifyOnMoveEndListeners(detector);
    verify(listener, times(1)).onMoveBegin(detector);
    verify(listener, times(1)).onMove(detector);
    verify(listener, times(1)).onMoveEnd(detector);

    mapGestureDetector.removeOnMoveListener(listener);
    mapGestureDetector.notifyOnMoveBeginListeners(detector);
    mapGestureDetector.notifyOnMoveListeners(detector);
    mapGestureDetector.notifyOnMoveEndListeners(detector);
    verify(listener, times(1)).onMoveBegin(detector);
    verify(listener, times(1)).onMove(detector);
    verify(listener, times(1)).onMoveEnd(detector);
  }

  @Test
  public void onRotateListenerTest() throws Exception {
    TrackasiaMap.OnRotateListener listener = mock(TrackasiaMap.OnRotateListener.class);
    RotateGestureDetector detector = mock(RotateGestureDetector.class);
    mapGestureDetector.addOnRotateListener(listener);
    mapGestureDetector.notifyOnRotateBeginListeners(detector);
    mapGestureDetector.notifyOnRotateListeners(detector);
    mapGestureDetector.notifyOnRotateEndListeners(detector);
    verify(listener, times(1)).onRotateBegin(detector);
    verify(listener, times(1)).onRotate(detector);
    verify(listener, times(1)).onRotateEnd(detector);

    mapGestureDetector.removeOnRotateListener(listener);
    mapGestureDetector.notifyOnRotateBeginListeners(detector);
    mapGestureDetector.notifyOnRotateListeners(detector);
    mapGestureDetector.notifyOnRotateEndListeners(detector);
    verify(listener, times(1)).onRotateBegin(detector);
    verify(listener, times(1)).onRotate(detector);
    verify(listener, times(1)).onRotateEnd(detector);
  }

  @Test
  public void onScaleListenerTest() throws Exception {
    TrackasiaMap.OnScaleListener listener = mock(TrackasiaMap.OnScaleListener.class);
    StandardScaleGestureDetector detector = mock(StandardScaleGestureDetector.class);
    mapGestureDetector.addOnScaleListener(listener);
    mapGestureDetector.notifyOnScaleBeginListeners(detector);
    mapGestureDetector.notifyOnScaleListeners(detector);
    mapGestureDetector.notifyOnScaleEndListeners(detector);
    verify(listener, times(1)).onScaleBegin(detector);
    verify(listener, times(1)).onScale(detector);
    verify(listener, times(1)).onScaleEnd(detector);

    mapGestureDetector.removeOnScaleListener(listener);
    mapGestureDetector.notifyOnScaleBeginListeners(detector);
    mapGestureDetector.notifyOnScaleListeners(detector);
    mapGestureDetector.notifyOnScaleEndListeners(detector);
    verify(listener, times(1)).onScaleBegin(detector);
    verify(listener, times(1)).onScale(detector);
    verify(listener, times(1)).onScaleEnd(detector);
  }

  @Test
  public void onShoveListenerTest() throws Exception {
    TrackasiaMap.OnShoveListener listener = mock(TrackasiaMap.OnShoveListener.class);
    ShoveGestureDetector detector = mock(ShoveGestureDetector.class);
    mapGestureDetector.addShoveListener(listener);
    mapGestureDetector.notifyOnShoveBeginListeners(detector);
    mapGestureDetector.notifyOnShoveListeners(detector);
    mapGestureDetector.notifyOnShoveEndListeners(detector);
    verify(listener, times(1)).onShoveBegin(detector);
    verify(listener, times(1)).onShove(detector);
    verify(listener, times(1)).onShoveEnd(detector);

    mapGestureDetector.removeShoveListener(listener);
    mapGestureDetector.notifyOnShoveBeginListeners(detector);
    mapGestureDetector.notifyOnShoveListeners(detector);
    mapGestureDetector.notifyOnShoveEndListeners(detector);
    verify(listener, times(1)).onShoveBegin(detector);
    verify(listener, times(1)).onShove(detector);
    verify(listener, times(1)).onShoveEnd(detector);
  }
}
