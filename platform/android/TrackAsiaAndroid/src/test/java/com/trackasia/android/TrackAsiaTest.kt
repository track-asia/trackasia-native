package com.trackasia.android

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import com.trackasia.android.TrackAsiaInjector.clear
import com.trackasia.android.TrackAsiaInjector.inject
import com.trackasia.android.exceptions.TrackAsiaConfigurationException
import com.trackasia.android.maps.MapView
import com.trackasia.android.utils.ConfigUtils.Companion.getMockedOptions
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class TrackAsiaTest {
    private var context: Context? = null
    private var appContext: Context? = null

    @Rule
    @JvmField // J2K: https://stackoverflow.com/a/33449455
    var expectedException = ExpectedException.none()

    @Before
    fun before() {
        context = Mockito.mock(Context::class.java)
        appContext = Mockito.mock(Context::class.java)
        // J2K: https://www.baeldung.com/kotlin/smart-cast-to-type-is-impossible#2-using-the-safe-call-operator--and-a-scope-function
        Mockito.`when`(context?.getApplicationContext()).thenReturn(appContext)
    }

    @Test
    fun testGetApiKey() {
        val apiKey = "pk.0000000001"
        inject(context!!, apiKey, getMockedOptions())
        Assert.assertSame(apiKey, TrackAsia.getApiKey())
    }

    @Test
    fun testApplicationContext() {
        inject(context!!, "pk.0000000001", getMockedOptions())
        Assert.assertNotNull(TrackAsia.getApplicationContext())
        Assert.assertNotEquals(context, appContext)
        Assert.assertEquals(appContext, appContext)
    }

    @Test
    fun testPlainTokenValid() {
        Assert.assertTrue(TrackAsia.isApiKeyValid("apiKey"))
    }

    @Test
    fun testEmptyToken() {
        Assert.assertFalse(TrackAsia.isApiKeyValid(""))
    }

    @Test
    fun testNullToken() {
        Assert.assertFalse(TrackAsia.isApiKeyValid(null))
    }

    @Test
    fun testNoInstance() {
        val displayMetrics = Mockito.mock(DisplayMetrics::class.java)
        val resources = Mockito.mock(Resources::class.java)
        Mockito.`when`(resources.displayMetrics).thenReturn(displayMetrics)
        Mockito.`when`(context!!.resources).thenReturn(resources)
        val typedArray = Mockito.mock(TypedArray::class.java)
        Mockito
            .`when`(
                context!!.obtainStyledAttributes(
                    ArgumentMatchers.nullable(AttributeSet::class.java),
                    ArgumentMatchers.any(IntArray::class.java),
                    ArgumentMatchers.anyInt(),
                    ArgumentMatchers.anyInt(),
                ),
            ).thenReturn(typedArray)
        expectedException.expect(TrackAsiaConfigurationException::class.java)
        expectedException.expectMessage(
            """

            Using MapView requires calling TrackAsia.getInstance(Context context, String apiKey, WellKnownTileServer wellKnownTileServer) before inflating or creating the view.
            """.trimIndent(),
        )
        MapView(context!!)
    }

    @After
    fun after() {
        clear()
    }
}
