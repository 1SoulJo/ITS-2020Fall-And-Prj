package com.humber.its2020.ibourit.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.humber.its2020.ibourit.entity.InventoryItem

@Dao
interface InventoryItemDao {
    @Query("SELECT * FROM inventoryitem")
    fun getAll(): List<InventoryItem>

    @Query("SELECT * FROM inventoryitem WHERE category == :category ORDER BY date DESC")
    fun loadAllByCategory(category: Int): LiveData<List<InventoryItem>>

    @Insert
    fun insertAll(vararg items: InventoryItem)

    @Delete
    fun delete(item: InventoryItem)

    @Insert
    fun save(item: InventoryItem): Long
}