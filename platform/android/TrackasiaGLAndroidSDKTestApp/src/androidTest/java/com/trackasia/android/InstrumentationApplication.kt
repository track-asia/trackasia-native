package com.trackasia.android

import com.trackasia.android.testapp.MapboxApplication

class InstrumentationApplication : MapboxApplication() {
    override fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
