package com.trackasia.android

import android.content.Context
import com.trackasia.android.util.TileServerOptions

object TrackAsiaInjector {
    private const val FIELD_INSTANCE = "INSTANCE"

    @JvmStatic
    fun inject(
        context: Context,
        apiKey: String,
        options: TileServerOptions,
    ) {
        val trackasia = TrackAsia(context, apiKey, options)
        try {
            val instance = TrackAsia::class.java.getDeclaredField(FIELD_INSTANCE)
            instance.isAccessible = true
            instance[trackasia] = trackasia
        } catch (exception: Exception) {
            throw AssertionError()
        }
    }

    @JvmStatic
    fun clear() {
        try {
            val field = TrackAsia::class.java.getDeclaredField(FIELD_INSTANCE)
            field.isAccessible = true
            field[field] = null
        } catch (exception: Exception) {
            throw AssertionError()
        }
    }
}
