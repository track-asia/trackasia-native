package com.trackasia.android.testapp.storage

import androidx.test.annotation.UiThreadTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.trackasia.android.AppCenter
import com.trackasia.android.storage.FileSource
import com.trackasia.android.testapp.activity.FeatureOverviewActivity
import org.junit.*
import org.junit.rules.TestName
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class FileSourceStandaloneTest : AppCenter() {
    private lateinit var fileSourceTestUtils: FileSourceTestUtils
    private lateinit var fileSource: FileSource

    @get:Rule
    val rule = ActivityTestRule(FeatureOverviewActivity::class.java)

    @get:Rule
    val testName = TestName()

    @Before
    @UiThreadTest
    fun setup() {
        fileSource = FileSource.getInstance(rule.activity)
        fileSourceTestUtils = FileSourceTestUtils(rule.activity)
        fileSourceTestUtils.setup()
    }

    @Test
    @UiThreadTest
    fun testDefault() {
        Assert.assertTrue("FileSource should be active", fileSource.isActivated)
    }

    @Test
    @UiThreadTest
    fun testActivateDeactivate() {
        Assert.assertTrue("1) FileSource should be active", fileSource.isActivated)
        fileSource.deactivate()
        Assert.assertFalse("2) FileSource should not be active", fileSource.isActivated)
        fileSource.activate()
        Assert.assertTrue("3) FileSource should be active", fileSource.isActivated)
    }

    @Test
    fun pathChangeTest() {
        Assert.assertTrue("FileSource should be active", fileSource.isActivated)
        Assert.assertEquals(fileSourceTestUtils.originalPath, FileSource.getResourcesCachePath(rule.activity))

        fileSourceTestUtils.changePath(fileSourceTestUtils.testPath)
        Assert.assertEquals(fileSourceTestUtils.testPath, FileSource.getResourcesCachePath(rule.activity))
        Assert.assertTrue("FileSource should be active", fileSource.isActivated)

        fileSourceTestUtils.changePath(fileSourceTestUtils.originalPath)
        Assert.assertEquals(fileSourceTestUtils.originalPath, FileSource.getResourcesCachePath(rule.activity))
        Assert.assertTrue("FileSource should be active", fileSource.isActivated)
    }

    @Test
    fun overridePathChangeCallTest() {
        val firstLatch = CountDownLatch(1)
        val secondLatch = CountDownLatch(1)
        rule.activity.runOnUiThread {
            FileSource.setResourcesCachePath(
                fileSourceTestUtils.testPath,
                object : FileSource.ResourcesCachePathChangeCallback {
                    override fun onSuccess(path: String) {
                        Assert.assertEquals(fileSourceTestUtils.testPath, path)
                        firstLatch.countDown()
                    }

                    override fun onError(message: String) {
                        Assert.fail("First attempt should succeed.")
                    }
                },
            )

            FileSource.setResourcesCachePath(
                fileSourceTestUtils.testPath2,
                object : FileSource.ResourcesCachePathChangeCallback {
                    override fun onSuccess(path: String) {
                        Assert.fail("Second attempt should fail because first one is in progress.")
                    }

                    override fun onError(message: String) {
                        Assert.assertEquals("Another resources cache path change is in progress", message)
                        secondLatch.countDown()
                    }
                },
            )
        }

        if (!secondLatch.await(5, TimeUnit.SECONDS)) {
            Assert.fail("Second attempt should fail.")
        }

        if (!firstLatch.await(5, TimeUnit.SECONDS)) {
            Assert.fail("First attempt should succeed.")
        }
    }

    @After
    fun cleanup() {
        fileSourceTestUtils.cleanup()
    }
}
