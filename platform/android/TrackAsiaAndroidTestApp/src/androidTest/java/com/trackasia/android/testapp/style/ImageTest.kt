package com.trackasia.android.testapp.style

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.trackasia.android.testapp.R
import com.trackasia.android.testapp.action.TrackAsiaMapAction
import com.trackasia.android.testapp.activity.EspressoTest
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * CRUD tests around Image
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ImageTest : EspressoTest() {
    companion object {
        private const val IMAGE_ID = "test.image"
    }

    @Test
    fun testAddGetImage() {
        validateTestSetup()
        TrackAsiaMapAction.invoke(trackasiaMap) { uiController, trackasiaMap ->
            val drawable = rule.activity.resources.getDrawable(R.drawable.ic_launcher_round, null)
            assertTrue(drawable is BitmapDrawable)

            val bitmapSet = (drawable as BitmapDrawable).bitmap
            trackasiaMap.style!!.addImage(IMAGE_ID, bitmapSet)

            // adding an image requires converting the image with an asynctask
            uiController.loopMainThreadForAtLeast(200)

            val bitmapGet = trackasiaMap.style!!.getImage(IMAGE_ID)
            assertTrue(bitmapGet!!.similarTo(bitmapSet))

            trackasiaMap.style!!.removeImage(IMAGE_ID)
            assertNull(trackasiaMap.style!!.getImage(IMAGE_ID))
        }
    }
}

/**
 * Alternative implementation of Bitmap.sameAs #14060
 */
fun Bitmap.similarTo(other: Bitmap): Boolean {
    if (invalidConfig(other)) {
        return false
    }

    // Allocate arrays
    val argb = IntArray(width * height)
    val argbOther = IntArray(other.width * other.height)
    getPixels(argb, 0, width, 0, 0, width, height)
    other.getPixels(argbOther, 0, width, 0, 0, width, height)

    // Alpha channel special check
    if (config == Bitmap.Config.ALPHA_8) {
        // in this case we have to manually compare the alpha channel as the rest is garbage.
        val length = width * height
        for (i in 0 until length) {
            if (argb[i] and -0x1000000 != argbOther[i] and -0x1000000) {
                return false
            }
        }
        return true
    }
    return Arrays.equals(argb, argbOther)
}

fun Bitmap.invalidConfig(other: Bitmap): Boolean = this.config != other.config || this.width != other.width || this.height != other.height
