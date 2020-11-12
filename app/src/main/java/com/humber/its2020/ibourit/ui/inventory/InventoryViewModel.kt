package com.humber.its2020.ibourit.ui.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.entity.Inventory

class InventoryViewModel : ViewModel() {
    private val inventories = MutableLiveData<List<Inventory>>()

    val adapter: InventoryAdapter = InventoryAdapter()

    init {
        loadInventories()
    }

    fun getInventories(): MutableLiveData<List<Inventory>> {
        return inventories
    }

    private fun loadInventories() {
        val list = ArrayList<Inventory>()
        list.add(Inventory(0, "Shoes", R.drawable.inven_shoes))
        list.add(Inventory(1, "Apparel", R.drawable.inven_apparel))
        list.add(Inventory(2, "Watch", R.drawable.inven_watch))
        list.add(Inventory(3, "Car", R.drawable.inven_car))
        list.add(Inventory(4, "Real Estate", R.drawable.inven_real_estate))
        list.add(Inventory(5, "Appliance", R.drawable.inven_appliance))
        list.add(Inventory(6, "Mobile Device", R.drawable.inven_mobile_device))
        list.add(Inventory(7, "Toy", R.drawable.inven_toy))
        list.add(Inventory(8, "Tech Gadget", R.drawable.inven_tech_gadget))

        inventories.value = list
    }
}