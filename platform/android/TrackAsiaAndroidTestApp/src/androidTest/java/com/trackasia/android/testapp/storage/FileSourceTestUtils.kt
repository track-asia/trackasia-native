package com.trackasia.android.testapp.storage

import android.app.Activity
import androidx.annotation.WorkerThread
import com.trackasia.android.AppCenter
import com.trackasia.android.storage.FileSource
import org.junit.Assert
import java.io.File
import java.util.concurrent.CountDownLatch

class FileSourceTestUtils(
    private val activity: Activity,
) : AppCenter() {
    val originalPath = FileSource.getResourcesCachePath(activity)
    val testPath = "$originalPath/test"
    val testPath2 = "$originalPath/test2"

    private val paths = listOf(testPath, testPath2)

    fun setup() {
        for (path in paths) {
            val testFile = File(path)
            testFile.mkdirs()
        }
    }

    @WorkerThread
    fun cleanup() {
        val currentPath = FileSource.getResourcesCachePath(activity)
        if (currentPath != originalPath) {
            changePath(originalPath)
        }

        for (path in paths) {
            val testFile = File(path)
            if (testFile.exists()) {
                testFile.deleteRecursively()
            }
        }
    }

    @WorkerThread
    fun changePath(requestedPath: String) {
        val latch = CountDownLatch(1)
        activity.runOnUiThread {
            FileSource.setResourcesCachePath(
                requestedPath,
                object : FileSource.ResourcesCachePathChangeCallback {
                    override fun onSuccess(path: String) {
                        Assert.assertEquals(requestedPath, path)
                        latch.countDown()
                    }

                    override fun onError(message: String) {
                        Assert.fail("Resource path change failed - path: $requestedPath, message: $message")
                    }
                },
            )
        }
        latch.await()
    }
}
