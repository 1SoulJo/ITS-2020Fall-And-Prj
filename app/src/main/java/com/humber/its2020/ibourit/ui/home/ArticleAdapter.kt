package com.humber.its2020.ibourit.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.animations.DescriptionAnimation
import com.glide.slider.library.slidertypes.DefaultSliderView
import com.glide.slider.library.slidertypes.TextSliderView
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.entity.Article
import kotlinx.android.synthetic.main.list_item_article2.view.*


class ArticleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val topLogo: ImageView = view.top_logo
        val topUser: TextView = view.top_user
        val slider: SliderLayout = view.slider
        val mainUser: TextView = view.main_user
        val mainContent: TextView = view.main_content
    }

    private lateinit var articles: List<Article>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_article2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ViewHolder
        h.topLogo.setImageResource(R.drawable.ic_user_default)

        val article = articles[position]
        h.topUser.text = "test user $position"
        h.mainUser.text = "test user $position"
        h.mainContent.text ="test content $position"

        initSlider(h, h.itemView.context)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(a: List<Article>) {
        articles = a
        notifyDataSetChanged()
    }

    private fun initSlider(h: ViewHolder, ctx: Context) {
        val listUrl: ArrayList<String> = ArrayList()

        listUrl.add("https://images.freeimages.com/images/small-previews/7e9/purple-flower-1395092.jpg")
        listUrl.add("https://images.freeimages.com/images/small-previews/4cb/paw-1394447.jpg")
        listUrl.add("https://images.freeimages.com/images/small-previews/56e/hibiscus-1393855.jpg")

        val requestOptions = RequestOptions()
        requestOptions.centerCrop()

        for (i in 0 until listUrl.size) {
            val sliderView = DefaultSliderView(ctx)

            sliderView
                .image(listUrl[i])
                .setRequestOption(requestOptions)
                .setProgressBarVisible(true)

            sliderView.bundle(Bundle())
            h.slider.addSlider(sliderView)
        }
        h.slider.setPresetTransformer(SliderLayout.Transformer.Default)

        h.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        h.slider.stopAutoCycle()
    }
}