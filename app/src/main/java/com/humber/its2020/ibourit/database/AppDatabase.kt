package com.humber.its2020.ibourit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.humber.its2020.ibourit.entity.InventoryItem
import com.humber.its2020.ibourit.entity.ItemImage

@Database(entities = [InventoryItem::class, ItemImage::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(ctx: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "inventory")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                // return instance
                instance
            }
        }
    }

    abstract fun itemDao(): InventoryItemDao
    abstract fun itemImageDao(): ItemImageDao
}