package com.febrian.covidapp.news.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntityNews::class], version = 2, exportSchema = false)
abstract class NewsRoomDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): NewsRoomDatabase {
            if (INSTANCE == null) {
                synchronized(NewsRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            NewsRoomDatabase::class.java, "news_database")
                            .allowMainThreadQueries() //allows Room to executing task in main thread
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE as NewsRoomDatabase
        }
    }
}