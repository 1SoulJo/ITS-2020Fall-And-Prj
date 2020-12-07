package com.humber.its2020.ibourit.ui.inventory

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
import com.humber.its2020.ibourit.entity.Inventory
import kotlinx.android.synthetic.main.fragment_inventory.*

class InventoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title =
            " " + resources.getString(R.string.inventory)

        val viewModel: InventoryViewModel by viewModels()
        viewModel.init(requireContext())
        rv_inventory.layoutManager = LinearLayoutManager(activity)
        rv_inventory.adapter = viewModel.adapter
        val observer = Observer<List<Inventory>> { data ->
            viewModel.adapter.setArticles(data)
        }
        viewModel.getInventories().observe(activity as LifecycleOwner, observer)
    }
}