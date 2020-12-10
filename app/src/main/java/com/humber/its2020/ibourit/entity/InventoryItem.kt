package com.humber.its2020.ibourit.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InventoryItem (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "date") val date: Long
)
