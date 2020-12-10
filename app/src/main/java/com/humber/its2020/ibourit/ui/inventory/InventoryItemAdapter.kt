package com.humber.its2020.ibourit.ui.inventory

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.with
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.database.AppDatabase
import com.humber.its2020.ibourit.entity.InventoryItem
import com.humber.its2020.ibourit.util.GlideApp
import kotlinx.android.synthetic.main.list_item_inventory_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InventoryItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items: List<InventoryItem>

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val image1: ImageView = view.item_img_1
        val image2: ImageView = view.item_img_2
        val image3: ImageView = view.item_img_3
        val brand: TextView = view.item_detail_brand
        val model: TextView = view.item_detail_model
        val price: TextView = view.item_detail_price
        val age: TextView = view.item_detail_age
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_inventory_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ViewHolder
        val item = items[position]
        val dao = AppDatabase.get(holder.itemView.context).itemImageDao()

        h.image2.visibility = View.GONE
        h.image3.visibility = View.GONE

        GlobalScope.launch {
            val images = dao.getByItemId(item.id)
            for ((index, image) in images.withIndex()) {
                launch(Dispatchers.Main.immediate) {
                    val target: ImageView = when(index) {
                        0 -> h.image1
                        1 -> {
                            h.image2.visibility = View.VISIBLE
                            h.image2
                        }
                        2 -> {
                            h.image3.visibility = View.VISIBLE
                            h.image3
                        }
                        else -> return@launch
                    }

                    GlideApp.with(holder.itemView.context)
                        .load(Uri.parse(image.uri))
                        .into(target)
                }
            }
        }

        h.brand.text = item.brand
        h.model.text = item.name
        h.price.text = "$ ${item.price}"
        val date = Date(item.date)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CANADA)
        Log.d("Inventory", sdf.parse("2019-12-24").time.toString())
        Log.d("Inventory", sdf.format(Date(item.date)).toString())
        h.age.text = sdf.format(date)
    }

    override fun getItemCount(): Int {
        return if (this::items.isInitialized) {
            items.size
        } else {
            0
        }
    }

    fun setItems(list: List<InventoryItem>) {
        items = list
        notifyDataSetChanged()
    }
}