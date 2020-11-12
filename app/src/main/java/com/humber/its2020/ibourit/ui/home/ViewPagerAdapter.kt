package com.humber.its2020.ibourit.ui.home

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.humber.its2020.ibourit.R


class ViewPagerAdapter: PagerAdapter() {
    private val images = listOf(
        R.drawable.shop1, R.drawable.shop2,
        R.drawable.shop3, R.drawable.shop4)

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val iv = ImageView(container.context)
        iv.setImageResource(images[position])
        iv.scaleType = ImageView.ScaleType.CENTER_CROP
        iv.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )

        container.addView(iv)

        return iv
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}