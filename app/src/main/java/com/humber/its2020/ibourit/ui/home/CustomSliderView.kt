package com.humber.its2020.ibourit.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.glide.slider.library.slidertypes.BaseSliderView
import com.humber.its2020.ibourit.R


class CustomSliderView(context: Context) : BaseSliderView(context) {
    override fun getView(): View {
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_slider_item, null)
        val target: ImageView =
            v.findViewById<View>(R.id.daimajia_slider_image) as ImageView
        bindEventAndShow(v, target as AppCompatImageView)
        return v
    }
}