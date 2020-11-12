package com.humber.its2020.ibourit.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.entity.Article
import kotlinx.android.synthetic.main.list_item_article.view.*

class ArticleAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val topLogo: ImageView = view.top_logo
        val topUser: TextView = view.top_user
        val pager: ViewPager = view.view_pager
        val mainUser: TextView = view.main_user
        val mainContent: TextView = view.main_content
        val tabLayout: TabLayout = view.tab_layout
    }

    private lateinit var articles: List<Article>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ViewHolder
        h.topLogo.setImageResource(R.drawable.ic_user)

        val article = articles[position]
        h.topUser.text = "test user $position"
        h.mainUser.text = "test user $position"
        h.mainContent.text ="test content $position"
        h.pager.adapter = ViewPagerAdapter()

        h.tabLayout.setupWithViewPager(h.pager)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(a: List<Article>) {
        articles = a
        notifyDataSetChanged()
    }
}