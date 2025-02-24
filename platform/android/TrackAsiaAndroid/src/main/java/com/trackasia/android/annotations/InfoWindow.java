package com.trackasia.android.annotations;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trackasia.android.R;
import com.trackasia.android.geometry.LatLng;
import com.trackasia.android.maps.MapView;
import com.trackasia.android.maps.TrackAsiaMap;

import java.lang.ref.WeakReference;

/**
 * {@code InfoWindow} is a tooltip shown when a {@link Marker} is tapped. Only
 * one info window is displayed at a time. When the user clicks on a marker, the currently open info
 * window will be closed and the new info window will be displayed. If the user clicks the same
 * marker while its info window is currently open, the info window will be closed.
 * <p>
 * The info window is drawn oriented against the device's screen, centered above its associated
 * marker by default. The default info window contains the title in bold and snippet text below the title.
 * While either the title and
 * snippet are optional, at least one is required to open the info window.
 * </p>
 * @deprecated As of 7.0.0,
 * use <a href="https://github.com/mapbox/mapbox-plugins-android/tree/master/plugin-annotation">
 *   TrackAsia Annotation Plugin</a> instead
 */
@Deprecated
public class InfoWindow {

  private WeakReference<Marker> boundMarker;
  private WeakReference<TrackAsiaMap> trackasiaMap;
  protected WeakReference<View> view;

  private float markerHeightOffset;
  private float markerWidthOffset;
  private float viewWidthOffset;
  private float viewHeightOffset;
  private PointF coordinates;
  private boolean isVisible;

  @LayoutRes
  private int layoutRes;

  InfoWindow(MapView mapView, int layoutResId, TrackAsiaMap trackasiaMap) {
    layoutRes = layoutResId;
    View view = LayoutInflater.from(mapView.getContext()).inflate(layoutResId, mapView, false);
    initialize(view, trackasiaMap);
  }

  InfoWindow(@NonNull View view, TrackAsiaMap trackasiaMap) {
    initialize(view, trackasiaMap);
  }

  private void initialize(@NonNull View view, TrackAsiaMap trackasiaMap) {
    this.trackasiaMap = new WeakReference<>(trackasiaMap);
    isVisible = false;
    this.view = new WeakReference<>(view);

    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        TrackAsiaMap trackasiaMap = InfoWindow.this.trackasiaMap.get();
        if (trackasiaMap != null) {
          TrackAsiaMap.OnInfoWindowClickListener onInfoWindowClickListener = trackasiaMap.getOnInfoWindowClickListener();
          boolean handledDefaultClick = false;
          if (onInfoWindowClickListener != null) {
            handledDefaultClick = onInfoWindowClickListener.onInfoWindowClick(getBoundMarker());
          }

          if (!handledDefaultClick) {
            // default behavior: close it when clicking on the tooltip:
            closeInfoWindow();
          }
        }
      }
    });

    view.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        TrackAsiaMap trackasiaMap = InfoWindow.this.trackasiaMap.get();
        if (trackasiaMap != null) {
          TrackAsiaMap.OnInfoWindowLongClickListener listener = trackasiaMap.getOnInfoWindowLongClickListener();
          if (listener != null) {
            listener.onInfoWindowLongClick(getBoundMarker());
          }
        }
        return true;
      }
    });
  }

  private void closeInfoWindow() {
    TrackAsiaMap mapbox = trackasiaMap.get();
    Marker marker = boundMarker.get();
    if (marker != null && mapbox != null) {
      mapbox.deselectMarker(marker);
    }
    close();
  }

  /**
   * Open the info window at the specified position.
   *
   * @param boundMarker The marker on which is hooked the view.
   * @param position    to place the window on the map.
   * @param offsetX     The offset of the view to the position, in pixels. This allows to offset
   *                    the view from the object position.
   * @param offsetY     The offset of the view to the position, in pixels. This allows to offset
   *                    the view from the object position.
   * @return this {@link InfoWindow}.
   */
  @NonNull
  InfoWindow open(@NonNull MapView mapView, Marker boundMarker, @NonNull LatLng position, int offsetX, int offsetY) {
    setBoundMarker(boundMarker);

    MapView.LayoutParams lp = new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
      MapView.LayoutParams.WRAP_CONTENT);

    TrackAsiaMap trackasiaMap = this.trackasiaMap.get();
    View view = this.view.get();
    if (view != null && trackasiaMap != null) {
      view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

      markerHeightOffset = offsetY;
      markerWidthOffset = -offsetX;

      // Calculate default Android x,y coordinate
      coordinates = trackasiaMap.getProjection().toScreenLocation(position);
      float x = coordinates.x - (view.getMeasuredWidth() / 2) + offsetX;
      float y = coordinates.y - view.getMeasuredHeight() + offsetY;

      if (view instanceof BubbleLayout) {
        // only apply repositioning/margin for InfoWindowView
        Resources resources = mapView.getContext().getResources();

        // get right/left popup window
        float rightSideInfowWindow = x + view.getMeasuredWidth();
        float leftSideInfoWindow = x;

        // get right/left map view
        final float mapRight = mapView.getRight();
        final float mapLeft = mapView.getLeft();

        float marginHorizontal = resources.getDimension(R.dimen.trackasia_infowindow_margin);
        float tipViewOffset = resources.getDimension(R.dimen.trackasia_infowindow_tipview_width) / 2;
        float tipViewMarginLeft = view.getMeasuredWidth() / 2 - tipViewOffset;

        boolean outOfBoundsLeft = false;
        boolean outOfBoundsRight = false;

        // only optimise margins if view is inside current viewport
        if (coordinates.x >= 0 && coordinates.x <= mapView.getWidth()
          && coordinates.y >= 0 && coordinates.y <= mapView.getHeight()) {

          // if out of bounds right
          if (rightSideInfowWindow > mapRight) {
            outOfBoundsRight = true;
            x -= rightSideInfowWindow - mapRight;
            tipViewMarginLeft += rightSideInfowWindow - mapRight + tipViewOffset;
            rightSideInfowWindow = x + view.getMeasuredWidth();
          }

          // fit screen left
          if (leftSideInfoWindow < mapLeft) {
            outOfBoundsLeft = true;
            x += mapLeft - leftSideInfoWindow;
            tipViewMarginLeft -= mapLeft - leftSideInfoWindow + tipViewOffset;
            leftSideInfoWindow = x;
          }

          // Add margin right
          if (outOfBoundsRight && mapRight - rightSideInfowWindow < marginHorizontal) {
            x -= marginHorizontal - (mapRight - rightSideInfowWindow);
            tipViewMarginLeft += marginHorizontal - (mapRight - rightSideInfowWindow) - tipViewOffset;
            leftSideInfoWindow = x;
          }

          // Add margin left
          if (outOfBoundsLeft && leftSideInfoWindow - mapLeft < marginHorizontal) {
            x += marginHorizontal - (leftSideInfoWindow - mapLeft);
            tipViewMarginLeft -= (marginHorizontal - (leftSideInfoWindow - mapLeft)) - tipViewOffset;
          }
        }

        // Adjust tipView
        ((BubbleLayout) view).setArrowPosition(tipViewMarginLeft);
      }

      // set anchor popupwindowview
      view.setX(x);
      view.setY(y);

      // Calculate x-offset and y-offset for update method
      viewWidthOffset = x - coordinates.x - offsetX;
      viewHeightOffset = -view.getMeasuredHeight() + offsetY;


      close(); // if it was already opened
      mapView.addView(view, lp);
      isVisible = true;
    }
    return this;
  }

  /**
   * Close this {@link InfoWindow} if it is visible, otherwise calling this will do nothing.
   *
   * @return This {@link InfoWindow}
   */
  @NonNull
  InfoWindow close() {
    TrackAsiaMap trackasiaMap = this.trackasiaMap.get();
    if (isVisible && trackasiaMap != null) {
      isVisible = false;
      View view = this.view.get();
      if (view != null && view.getParent() != null) {
        ((ViewGroup) view.getParent()).removeView(view);
      }

      Marker marker = getBoundMarker();
      TrackAsiaMap.OnInfoWindowCloseListener listener = trackasiaMap.getOnInfoWindowCloseListener();
      if (listener != null) {
        listener.onInfoWindowClose(marker);
      }

      setBoundMarker(null);
    }
    return this;
  }

  /**
   * Constructs the view that is displayed when the InfoWindow opens. This retrieves data from
   * overlayItem and shows it in the tooltip.
   *
   * @param overlayItem the tapped overlay item
   */
  void adaptDefaultMarker(@NonNull Marker overlayItem, TrackAsiaMap trackasiaMap, @NonNull MapView mapView) {
    View view = this.view.get();
    if (view == null) {
      view = LayoutInflater.from(mapView.getContext()).inflate(layoutRes, mapView, false);
      initialize(view, trackasiaMap);
    }
    this.trackasiaMap = new WeakReference<>(trackasiaMap);
    String title = overlayItem.getTitle();
    TextView titleTextView = ((TextView) view.findViewById(R.id.infowindow_title));
    if (!TextUtils.isEmpty(title)) {
      titleTextView.setText(title);
      titleTextView.setVisibility(View.VISIBLE);
    } else {
      titleTextView.setVisibility(View.GONE);
    }

    String snippet = overlayItem.getSnippet();
    TextView snippetTextView = ((TextView) view.findViewById(R.id.infowindow_description));
    if (!TextUtils.isEmpty(snippet)) {
      snippetTextView.setText(snippet);
      snippetTextView.setVisibility(View.VISIBLE);
    } else {
      snippetTextView.setVisibility(View.GONE);
    }
  }

  @NonNull
  InfoWindow setBoundMarker(Marker boundMarker) {
    this.boundMarker = new WeakReference<>(boundMarker);
    return this;
  }

  @Nullable
  Marker getBoundMarker() {
    if (boundMarker == null) {
      return null;
    }
    return boundMarker.get();
  }

  /**
   * Will result in getting this {@link InfoWindow} and updating the view being displayed.
   */
  public void update() {
    TrackAsiaMap trackasiaMap = this.trackasiaMap.get();
    Marker marker = boundMarker.get();
    View view = this.view.get();
    if (trackasiaMap != null && marker != null && view != null) {
      coordinates = trackasiaMap.getProjection().toScreenLocation(marker.getPosition());

      if (view instanceof BubbleLayout) {
        view.setX(coordinates.x + viewWidthOffset - markerWidthOffset);
      } else {
        view.setX(coordinates.x - (view.getMeasuredWidth() / 2) - markerWidthOffset);
      }
      view.setY(coordinates.y + viewHeightOffset);
    }
  }

  void onContentUpdate() {
    //recalculate y-offset and update position
    View view = this.view.get();
    if (view != null) {
      ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
      if (viewTreeObserver.isAlive()) {
        viewTreeObserver.addOnGlobalLayoutListener(contentUpdateListener);
      }
    }
  }

  @Nullable
  private final ViewTreeObserver.OnGlobalLayoutListener contentUpdateListener =
    new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        View view = InfoWindow.this.view.get();
        if (view != null) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          }
          viewHeightOffset = -view.getMeasuredHeight() + markerHeightOffset;
          update();
        }
      }
    };

  /**
   * Retrieve this {@link InfoWindow}'s current view being used.
   *
   * @return This {@link InfoWindow}'s current View.
   */
  @Nullable
  public View getView() {
    return view != null ? view.get() : null;
  }

  boolean isVisible() {
    return isVisible;
  }

}
