package com.humber.its2020.ibourit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.entity.Article
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_article.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateActionbar()

        val viewModel: HomeViewModel by viewModels()
        rv_articles.layoutManager = LinearLayoutManager(activity)
        rv_articles.adapter = viewModel.adapter
        val observer = Observer<List<Article>> { data ->
            viewModel.adapter.setArticles(data)
        }
        viewModel.getArticles().observe(activity as LifecycleOwner, observer)
    }

    private fun updateActionbar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            " " + resources.getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setLogo(R.drawable.ic_bag_09)
        (activity as AppCompatActivity).supportActionBar?.setDisplayUseLogoEnabled(true)
    }
}