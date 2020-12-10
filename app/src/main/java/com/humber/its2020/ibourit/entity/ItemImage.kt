package com.humber.its2020.ibourit.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemImage (
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "item_id")
    val itemId: Int,

    @ColumnInfo(name = "uri")
    val uri: String
)