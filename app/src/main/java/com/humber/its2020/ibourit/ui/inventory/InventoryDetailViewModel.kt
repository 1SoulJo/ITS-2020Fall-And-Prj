package com.humber.its2020.ibourit.ui.inventory

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humber.its2020.ibourit.database.AppDatabase
import com.humber.its2020.ibourit.entity.InventoryItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InventoryDetailViewModel : ViewModel() {
    private lateinit var items : LiveData<List<InventoryItem>>

    val adapter: InventoryItemAdapter = InventoryItemAdapter()

    fun init(ctx: Context, category: Int) {
        items = AppDatabase.get(ctx).itemDao().loadAllByCategory(category)
    }

    fun getItems(): LiveData<List<InventoryItem>> {
        return items
    }

    fun setItems(list: List<InventoryItem>) {
        adapter.setItems(list)
        adapter.notifyDataSetChanged()
    }
}