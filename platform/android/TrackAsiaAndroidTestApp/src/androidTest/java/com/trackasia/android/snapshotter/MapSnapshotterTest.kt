package com.trackasia.android.snapshotter

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.Style
import com.trackasia.android.style.layers.BackgroundLayer
import com.trackasia.android.style.layers.PropertyFactory
import com.trackasia.android.testapp.activity.FeatureOverviewActivity
import com.trackasia.android.testapp.styles.TestStyles
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Integration test that validates if a snapshotter creation
 */
@Ignore("https://github.com/trackasia/trackasia-native/issues/2317")
@RunWith(AndroidJUnit4ClassRunner::class)
class MapSnapshotterTest {
    @Rule
    @JvmField
    var rule = ActivityTestRule(FeatureOverviewActivity::class.java)

    private val countDownLatch = CountDownLatch(1)

    @Test
    fun mapSnapshotter() {
        var mapSnapshotter: MapSnapshotter?
        rule.activity.runOnUiThread {
            val bg = BackgroundLayer("rand_tint")
            bg.setProperties(PropertyFactory.backgroundColor("rgba(255,128,0,0.7)"))
            val options =
                MapSnapshotter
                    .Options(512, 512)
                    .withPixelRatio(1.0f)
                    .withStyleBuilder(
                        Style
                            .Builder()
                            .fromUri(TestStyles.getPredefinedStyleWithFallback("Satellite Hybrid"))
                            .withLayerAbove(bg, "country-label"),
                    ).withCameraPosition(
                        CameraPosition
                            .Builder()
                            .zoom(12.0)
                            .target(LatLng(51.145495, 5.742234))
                            .build(),
                    )
            mapSnapshotter = MapSnapshotter(rule.activity, options)
            mapSnapshotter!!.start(
                {
                    assertNotNull(it)
                    assertNotNull(it.bitmap)
                    countDownLatch.countDown()
                },
                {
                    Assert.fail(it)
                },
            )
        }
        if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
            throw TimeoutException()
        }
    }
}
