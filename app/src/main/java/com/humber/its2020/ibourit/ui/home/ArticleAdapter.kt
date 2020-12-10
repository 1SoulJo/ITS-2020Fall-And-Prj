package com.humber.its2020.ibourit.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.indicators.PagerIndicator
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.Urls.IMAGE_BASE
import com.humber.its2020.ibourit.credential.Credential
import com.humber.its2020.ibourit.entity.Article
import com.humber.its2020.ibourit.util.GlideApp
import com.humber.its2020.ibourit.web.ApiClient
import kotlinx.android.synthetic.main.list_item_article.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArticleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val topLogo: ImageView = view.top_logo
        val topUser: TextView = view.top_user
        val fixedImage: ImageView = view.fixed_image
        val slider: SliderLayout = view.slider
        val mainUser: TextView = view.main_user
        val mainContent: TextView = view.main_content
        val info: LinearLayout = view.info_layout
        val more: ImageView = view.top_more
    }

    private lateinit var articles: List<Article>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article = articles[position]

        val h = holder as ViewHolder
        h.topLogo.setImageResource(R.drawable.ic_user_default)
        h.topUser.text = article.userName
        h.mainUser.text = article.userName
        h.mainContent.text = article.description
        h.info.visibility = View.GONE

        initImages(article, h, h.itemView.context)

        h.info.info_brand.text = article.brand
        h.info.info_name.text = article.name
        h.info.info_price.text = "$ ${article.price}"
        h.fixedImage.setOnClickListener {
            if (h.info.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(h.itemView as ViewGroup, Fade(Fade.OUT))
                h.info.visibility = View.GONE
            } else {
                TransitionManager.beginDelayedTransition(h.itemView as ViewGroup, Fade(Fade.IN))
                h.info.visibility = View.VISIBLE
            }
        }

        if (Credential.id() == article.userId) {
            h.more.visibility = View.VISIBLE
            h.more.setOnClickListener {
                val popup = PopupMenu(h.itemView.context, h.more)
                popup.menuInflater.inflate(R.menu.article_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    if (it.itemId == R.id.article_menu_delete) {
                        ApiClient.deleteArticle(article.id, object: Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                notifyItemRemoved(position)
                            }
                            override fun onFailure(call: Call<Void>, t: Throwable) {}
                        })
                    }
                    true
                }
                popup.show()
            }
        } else {
            h.more.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return if (this::articles.isInitialized) {
            articles.size
        } else {
            0
        }
    }

    fun setArticles(a: List<Article>) {
        articles = a
        notifyDataSetChanged()
    }

    private fun initImages(a: Article, h: ViewHolder, ctx: Context) {
        if (a.images!!.size == 1) {
            h.fixedImage.visibility = View.VISIBLE
            h.slider.visibility = View.GONE

            GlideApp.with(ctx)
                .load(IMAGE_BASE.format(a.images[0]))
                .into(h.fixedImage)

        } else {
            h.fixedImage.visibility = View.GONE
            h.slider.visibility = View.VISIBLE
            h.slider.removeAllSliders()

            val listUrl: ArrayList<String> = ArrayList()

            for (image in a.images!!) {
                listUrl.add(IMAGE_BASE.format(image))
            }

            val requestOptions = RequestOptions()
            requestOptions.centerCrop()

            for (i in 0 until listUrl.size) {
                val sliderView = CustomSliderView(ctx)

                sliderView
                    .image(listUrl[i])
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener {
                        if (h.info.visibility == View.VISIBLE) {
                            TransitionManager.beginDelayedTransition(
                                h.itemView as ViewGroup, Fade(
                                    Fade.OUT
                                )
                            )
                            h.info.visibility = View.GONE
                        } else {
                            TransitionManager.beginDelayedTransition(
                                h.itemView as ViewGroup, Fade(
                                    Fade.IN
                                )
                            )
                            h.info.visibility = View.VISIBLE
                        }
                    }

                sliderView.bundle(Bundle())
                h.slider.addSlider(sliderView)
            }

            h.slider.indicatorVisibility = PagerIndicator.IndicatorVisibility.Visible
            h.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            h.slider.pagerIndicator.setDefaultIndicatorColor(
                Color.argb(180, 255, 0, 0),
                Color.argb(33, 128, 128, 128)
            )

            h.slider.setPresetTransformer(SliderLayout.Transformer.DepthPage)
            h.slider.stopAutoCycle()
        }
    }
}