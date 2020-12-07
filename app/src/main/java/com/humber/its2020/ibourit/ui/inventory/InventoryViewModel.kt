package com.humber.its2020.ibourit.ui.inventory

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.entity.Inventory
import java.util.*
import kotlin.collections.ArrayList

class InventoryViewModel : ViewModel() {
    private val inventories = MutableLiveData<List<Inventory>>()

    val adapter: InventoryAdapter = InventoryAdapter()

    fun getInventories(): MutableLiveData<List<Inventory>> {
        return inventories
    }

    fun init(ctx: Context) {
        val list = ArrayList<Inventory>()

        val categories = ctx.resources.getStringArray(R.array.categories)
        val imgIds = ctx.resources.obtainTypedArray(R.array.category_img)
        for ((i, c) in categories.withIndex()) {
            list.add(Inventory(c, imgIds.getResourceId(i, 0)))
        }

        inventories.value = list
    }
}