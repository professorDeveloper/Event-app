package com.azamovhudstc.eventapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.azamovhudstc.eventapp.data.local.dao.EventDao
import com.azamovhudstc.eventapp.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        private lateinit var instance: EventDatabase

        fun getDatabase(context: Context): EventDatabase {
            if (Companion::instance.isInitialized) return instance
            return Room
                .databaseBuilder(context, EventDatabase::class.java, context.packageName)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}