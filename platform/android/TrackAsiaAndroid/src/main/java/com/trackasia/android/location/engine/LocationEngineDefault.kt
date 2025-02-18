package com.trackasia.android.location.engine

import android.content.Context

object LocationEngineDefault {
    /**
     * Returns the default `LocationEngine`.
     */
    fun getDefaultLocationEngine(context: Context): LocationEngine =
        LocationEngineProxy(
            TrackAsiaFusedLocationEngineImpl(
                context.applicationContext,
            ),
        )
}
