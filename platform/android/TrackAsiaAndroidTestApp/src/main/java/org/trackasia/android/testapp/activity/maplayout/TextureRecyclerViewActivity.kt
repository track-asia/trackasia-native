package org.trackasia.android.testapp.activity.maplayout

import android.annotation.SuppressLint
import org.trackasia.android.testapp.R

/**
 * TestActivity showcasing how to integrate multiple TexureView MapViews in a RecyclerView.
 */
@SuppressLint("ClickableViewAccessibility")
class TextureRecyclerViewActivity : GLSurfaceRecyclerViewActivity() {

    override fun getMapItemLayoutId(): Int {
        return R.layout.item_map_texture
    }
}
