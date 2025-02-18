package com.trackasia.android.benchmark

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.trackasia.android.testapp.activity.benchmark.BenchmarkActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class Benchmark {
    @Test
    fun worldTourBenchmark() {
        val scenario = ActivityScenario.launchActivityForResult(BenchmarkActivity::class.java)
        while (scenario.state !== Lifecycle.State.DESTROYED) {
            Thread.sleep(1000)
        }
        assertEquals(scenario.result.resultCode, Activity.RESULT_OK)
    }
}
