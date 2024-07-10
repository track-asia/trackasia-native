package org.trackasia.android.location

import android.animation.TypeEvaluator
import androidx.annotation.Size
import org.trackasia.android.maps.TrackAsiaMap.CancelableCallback

class TrackAsiaPaddingAnimator internal constructor(
    @Size(min = 2) values: Array<DoubleArray>,
    updateListener: AnimationsValueChangeListener<DoubleArray>,
    cancelableCallback: CancelableCallback?,
) :
    TrackAsiaAnimator<DoubleArray>(values, updateListener, Int.MAX_VALUE) {
        init {
            addListener(TrackAsiaAnimatorListener(cancelableCallback))
        }

        public override fun provideEvaluator(): TypeEvaluator<DoubleArray> {
            return PaddingEvaluator()
        }
    }
