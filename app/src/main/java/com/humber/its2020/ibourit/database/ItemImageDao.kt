package com.humber.its2020.ibourit.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.humber.its2020.ibourit.entity.ItemImage

@Dao
interface ItemImageDao {
    @Query("SELECT * FROM itemimage WHERE item_id == :id")
    fun getByItemId(id: Int): List<ItemImage>

    @Query("SELECT * FROM itemimage WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<ItemImage>

    @Insert
    fun insertAll(vararg images: ItemImage)

    @Delete
    fun delete(image: ItemImage)

    @Insert
    fun save(image: ItemImage)
}