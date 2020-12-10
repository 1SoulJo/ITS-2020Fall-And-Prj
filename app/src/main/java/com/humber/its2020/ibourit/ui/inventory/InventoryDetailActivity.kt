package com.humber.its2020.ibourit.ui.inventory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.Category
import kotlinx.android.synthetic.main.activity_inventory_detail.*

class InventoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_detail)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        val category = intent.getStringExtra("category")

        supportActionBar?.title = " $category"
        supportActionBar?.setLogo(R.drawable.ic_bag_09)

        val viewModel: InventoryDetailViewModel by viewModels()
        rv_inventory_items.layoutManager = LinearLayoutManager(this)
        rv_inventory_items.adapter = viewModel.adapter
        viewModel.init(this, Category.byName(category!!).ordinal)
        viewModel.getItems().observe(this, {
            viewModel.setItems(it)
            if (it.isEmpty()) {
                empty_layout.visibility = View.VISIBLE
            } else {
                empty_layout.visibility = View.GONE
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inventory, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_new_inventory) {
            val i = Intent(this, AddInventoryActivity::class.java)
            i.putExtra("category", intent.getStringExtra("category"))
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }
}