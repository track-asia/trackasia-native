package com.trackasia.android.maps;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trackasia.android.utils.MapFragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Support Fragment wrapper around a map view.
 * <p>
 * A Map component in an app. This fragment is the simplest way to place a map in an application.
 * It's a wrapper around a view of a map to automatically handle the necessary life cycle needs.
 * Being a fragment, this component can be added to an activity's layout or can dynamically be added
 * using a FragmentManager.
 * </p>
 * <p>
 * To get a reference to the MapView, use {@link #getMapAsync(OnMapReadyCallback)}}
 * </p>
 *
 * @see #getMapAsync(OnMapReadyCallback)
 */
public class SupportMapFragment extends Fragment implements OnMapReadyCallback {

  private final List<OnMapReadyCallback> mapReadyCallbackList = new ArrayList<>();
  private MapFragment.OnMapViewReadyCallback mapViewReadyCallback;
  private TrackAsiaMap trackasiaMap;
  private MapView map;

  /**
   * Creates a default MapFragment instance
   *
   * @return MapFragment created
   */
  public static SupportMapFragment newInstance() {
    return new SupportMapFragment();
  }

  /**
   * Creates a MapFragment instance
   *
   * @param trackasiaMapOptions The configuration options to be used.
   * @return MapFragment created.
   */
  @NonNull
  public static SupportMapFragment newInstance(@Nullable TrackAsiaMapOptions trackasiaMapOptions) {
    SupportMapFragment mapFragment = new SupportMapFragment();
    mapFragment.setArguments(MapFragmentUtils.createFragmentArgs(trackasiaMapOptions));
    return mapFragment;
  }

  /**
   * Called when the context attaches to this fragment.
   *
   * @param context the context attaching
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof MapFragment.OnMapViewReadyCallback) {
      mapViewReadyCallback = (MapFragment.OnMapViewReadyCallback) context;
    }
  }

  /**
   * Called when this fragment is inflated, parses XML tag attributes.
   *
   * @param context            The context inflating this fragment.
   * @param attrs              The XML tag attributes.
   * @param savedInstanceState The saved instance state for the map fragment.
   */
  @Override
  public void onInflate(@NonNull Context context, AttributeSet attrs, Bundle savedInstanceState) {
    super.onInflate(context, attrs, savedInstanceState);
    setArguments(MapFragmentUtils.createFragmentArgs(TrackAsiaMapOptions.createFromAttributes(context, attrs)));
  }

  /**
   * Creates the fragment view hierarchy.
   *
   * @param inflater           Inflater used to inflate content.
   * @param container          The parent layout for the map fragment.
   * @param savedInstanceState The saved instance state for the map fragment.
   * @return The view created
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    Context context = inflater.getContext();
    map = new MapView(context, MapFragmentUtils.resolveArgs(context, getArguments()));
    return map;
  }

  /**
   * Called when the fragment view hierarchy is created.
   *
   * @param view               The content view of the fragment
   * @param savedInstanceState THe saved instance state of the framgnt
   */
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    map.onCreate(savedInstanceState);
    map.getMapAsync(this);

    // notify listeners about MapView creation
    if (mapViewReadyCallback != null) {
      mapViewReadyCallback.onMapViewReady(map);
    }
  }

  @Override
  public void onMapReady(@NonNull TrackAsiaMap trackasiaMap) {
    this.trackasiaMap = trackasiaMap;
    for (OnMapReadyCallback onMapReadyCallback : mapReadyCallbackList) {
      onMapReadyCallback.onMapReady(trackasiaMap);
    }
  }

  /**
   * Called when the fragment is visible for the users.
   */
  @Override
  public void onStart() {
    super.onStart();
    map.onStart();
  }

  /**
   * Called when the fragment is ready to be interacted with.
   */
  @Override
  public void onResume() {
    super.onResume();
    map.onResume();
  }

  /**
   * Called when the fragment is pausing.
   */
  @Override
  public void onPause() {
    super.onPause();
    map.onPause();
  }

  /**
   * Called when the fragment state needs to be saved.
   *
   * @param outState The saved state
   */
  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (map != null) {
      map.onSaveInstanceState(outState);
    }
  }

  /**
   * Called when the fragment is no longer visible for the user.
   */
  @Override
  public void onStop() {
    super.onStop();
    map.onStop();
  }

  /**
   * Called when the fragment receives onLowMemory call from the hosting Activity.
   */
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    if (map != null) {
      map.onLowMemory();
    }
  }

  /**
   * Called when the fragment is view hierarchy is being destroyed.
   */
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    map.onDestroy();
  }

  /**
   * Called when the fragment is destroyed.
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    mapReadyCallbackList.clear();
  }

  /**
   * Sets a callback object which will be triggered when the TrackAsiaMap instance is ready to be used.
   *
   * @param onMapReadyCallback The callback to be invoked.
   */
  public void getMapAsync(@NonNull final OnMapReadyCallback onMapReadyCallback) {
    if (trackasiaMap == null) {
      mapReadyCallbackList.add(onMapReadyCallback);
    } else {
      onMapReadyCallback.onMapReady(trackasiaMap);
    }
  }
}
