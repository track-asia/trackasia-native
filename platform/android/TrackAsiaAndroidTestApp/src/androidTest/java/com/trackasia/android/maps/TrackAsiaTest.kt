package com.trackasia.android.maps

import androidx.test.annotation.UiThreadTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.trackasia.android.AppCenter
import com.trackasia.android.TrackAsia
import com.trackasia.android.exceptions.TrackAsiaConfigurationException
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class TrackAsiaTest : AppCenter() {
    private var realToken: String? = null

    @Before
    fun setup() {
        realToken = TrackAsia.getApiKey()
    }

    @Test
    @UiThreadTest
    fun testConnected() {
        Assert.assertTrue(TrackAsia.isConnected())

        // test manual connectivity
        TrackAsia.setConnected(true)
        Assert.assertTrue(TrackAsia.isConnected())
        TrackAsia.setConnected(false)
        Assert.assertFalse(TrackAsia.isConnected())

        // reset to Android connectivity
        TrackAsia.setConnected(null)
        Assert.assertTrue(TrackAsia.isConnected())
    }

    @Test
    @UiThreadTest
    fun setApiKey() {
        TrackAsia.setApiKey(API_KEY)
        Assert.assertSame(API_KEY, TrackAsia.getApiKey())
        TrackAsia.setApiKey(API_KEY_2)
        Assert.assertSame(API_KEY_2, TrackAsia.getApiKey())
    }

    @Test
    @UiThreadTest
    fun setNullApiKey() {
        Assert.assertThrows(
            TrackAsiaConfigurationException::class.java,
        ) { TrackAsia.setApiKey(null) }
    }

    @After
    fun tearDown() {
        if (realToken?.isNotEmpty() == true) {
            TrackAsia.setApiKey(realToken)
        }
    }

    companion object {
        private const val API_KEY = "pk.0000000001"
        private const val API_KEY_2 = "pk.0000000002"
    }
}
