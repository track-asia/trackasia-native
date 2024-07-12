package com.mapbox.mapboxsdk

import com.mapbox.mapboxsdk.testapp.TrackAsiaApplication

class InstrumentationApplication : TrackAsiaApplication() {
    fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
