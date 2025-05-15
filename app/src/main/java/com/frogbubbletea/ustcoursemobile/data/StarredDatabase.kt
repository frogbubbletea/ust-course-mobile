package com.frogbubbletea.ustcoursemobile.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class StarredDatabase: RoomDatabase() {
    abstract fun starredDao(): StarredDao

//    companion object {
//        @Volatile
//        private var Instance: StarredDatabase? = null
//
//        fun getDatabase(context: Context): StarredDatabase {
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, StarredDatabase::class.java, "starred_database")
//                    .build()
//                    .also { Instance = it }
//            }
//        }
//    }
}