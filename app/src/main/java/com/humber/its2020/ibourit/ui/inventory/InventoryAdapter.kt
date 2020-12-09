package com.humber.its2020.ibourit.ui.inventory

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.entity.Inventory
import kotlinx.android.synthetic.main.list_item_inventory.view.*

class InventoryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var inventories: List<Inventory>

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.inventory_name
        val image: ImageView = view.inventory_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_inventory, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ViewHolder
        val inventory = inventories[position]
        h.name.text = inventory.name
        h.image.setImageResource(inventory.imgId)
        h.itemView.setOnClickListener {
            val i = Intent(h.itemView.context, InventoryDetailActivity::class.java)
            h.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return inventories.size
    }

    fun setInventories(i: List<Inventory>) {
        inventories = i
        notifyDataSetChanged()
    }
}