package org.trackasia.android.testapp.activity.render

import android.content.Context
import org.trackasia.android.snapshotter.MapSnapshot
import org.trackasia.android.snapshotter.MapSnapshotter

class RenderTestSnapshotter internal constructor(context: Context, options: Options) :
    MapSnapshotter(context, options) {
        override fun addOverlay(mapSnapshot: MapSnapshot) {
            // don't add an overlay
        }
    }
