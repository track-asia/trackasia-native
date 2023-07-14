package org.trackasia.android.location;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;

import org.trackasia.android.maps.trackasiaMap;

class trackasiaCameraAnimatorAdapter extends trackasiaFloatAnimator {
  @Nullable
  private final trackasiaMap.CancelableCallback cancelableCallback;

  trackasiaCameraAnimatorAdapter(@NonNull @Size(min = 2) Float[] values,
                                AnimationsValueChangeListener updateListener,
                                @Nullable trackasiaMap.CancelableCallback cancelableCallback) {
    super(values, updateListener, Integer.MAX_VALUE);
    this.cancelableCallback = cancelableCallback;
    addListener(new trackasiaAnimatorListener());
  }

  private final class trackasiaAnimatorListener extends AnimatorListenerAdapter {
    @Override
    public void onAnimationCancel(Animator animation) {
      if (cancelableCallback != null) {
        cancelableCallback.onCancel();
      }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
      if (cancelableCallback != null) {
        cancelableCallback.onFinish();
      }
    }
  }
}
