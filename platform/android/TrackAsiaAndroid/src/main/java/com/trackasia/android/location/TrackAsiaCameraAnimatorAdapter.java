package com.trackasia.android.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import com.trackasia.android.maps.TrackAsiaMap;

class TrackAsiaCameraAnimatorAdapter extends TrackAsiaFloatAnimator {

  TrackAsiaCameraAnimatorAdapter(@NonNull @Size(min = 2) Float[] values,
                                AnimationsValueChangeListener updateListener,
                                @Nullable TrackAsiaMap.CancelableCallback cancelableCallback) {
    super(values, updateListener, Integer.MAX_VALUE);
    addListener(new TrackAsiaAnimatorListener(cancelableCallback));
  }
}
