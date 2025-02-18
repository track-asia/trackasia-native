package com.trackasia.android.testapp.activity.maplayout

import android.annotation.SuppressLint
import com.trackasia.android.testapp.R

/**
 * TestActivity showcasing how to integrate multiple TexureView MapViews in a RecyclerView.
 */
@SuppressLint("ClickableViewAccessibility")
class TextureRecyclerViewActivity : GLSurfaceRecyclerViewActivity() {
    override fun getMapItemLayoutId(): Int = R.layout.item_map_texture
}
