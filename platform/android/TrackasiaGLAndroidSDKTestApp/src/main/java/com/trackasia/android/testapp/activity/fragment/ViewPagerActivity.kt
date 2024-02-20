package com.trackasia.android.testapp.activity.fragment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trackasia.android.camera.CameraPosition
import com.trackasia.android.geometry.LatLng
import com.trackasia.android.maps.TrackasiaMapOptions
import com.trackasia.android.maps.Style
import com.trackasia.android.maps.SupportMapFragment
import com.trackasia.android.testapp.databinding.ActivityViewpagerBinding

/**
 * Test activity showcasing using the Android SDK ViewPager API to show MapFragments.
 */
class ViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewpagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewpagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = MapFragmentAdapter(this, supportFragmentManager)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val currentPosition = binding.viewPager.currentItem
        val offscreenLimit = binding.viewPager.offscreenPageLimit
        for (i in currentPosition - offscreenLimit..currentPosition + offscreenLimit) {
            if (i < 0 || i > binding.viewPager.adapter?.count ?: 0) {
                continue
            }
            val mapFragment =
                binding.viewPager.adapter?.instantiateItem(binding.viewPager, i) as SupportMapFragment
            mapFragment.getMapAsync(i)
        }
    }

    internal class MapFragmentAdapter(private val context: Context, fragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentStatePagerAdapter(fragmentManager) {

        override fun getCount(): Int {
            return NUM_ITEMS
        }

        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            val options = TrackasiaMapOptions.createFromAttributes(context)
            options.textureMode(true)
            options.camera(
                CameraPosition.Builder()
                    .zoom(3.0)
                    .target(
                        when (position) {
                            0 -> {
                                LatLng(34.920526, 102.634774)
                            }
                            1 -> {
                                LatLng(62.326440, 92.764913)
                            }
                            2 -> {
                                LatLng(-25.007786, 133.623852)
                            }
                            3 -> {
                                LatLng(62.326440, 92.764913)
                            }
                            else -> {
                                LatLng(34.920526, 102.634774)
                            }
                        }
                    )
                    .build()
            )

            val fragment = SupportMapFragment.newInstance(options)
            fragment.getMapAsync(position)
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return "Page $position"
        }

        companion object {
            private const val NUM_ITEMS = 5
        }
    }
}

fun SupportMapFragment.getMapAsync(index: Int) {
    this.getMapAsync {it.setStyle("https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public")
//        it.setStyle(
//            when (index) {
//                0 -> Style.getPredefinedStyle("Streets")
//                1 -> "https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public"
//                2 -> "https://tiles.track-asia.com/tiles/v1/style-streets.json?key=public"
//                3 -> Style.getPredefinedStyle("Bright")
//                else -> Style.getPredefinedStyle("Streets")
//            }
//        )
    }
}
