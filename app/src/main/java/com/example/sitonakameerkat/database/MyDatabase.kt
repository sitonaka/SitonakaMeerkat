package com.example.sitonakameerkat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sitonakameerkat.server.GHUsers

@Database(entities = arrayOf(GHUsers::class), version = 1, exportSchema = false)
public abstract class MyDatabase : RoomDatabase() {

    abstract fun ghDao(): GHDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "my_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
