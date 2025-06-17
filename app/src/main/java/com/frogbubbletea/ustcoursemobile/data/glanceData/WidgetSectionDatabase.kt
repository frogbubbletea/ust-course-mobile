package com.frogbubbletea.ustcoursemobile.data.glanceData

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WidgetSectionEntity::class], version = 1, exportSchema = false)
abstract class WidgetSectionDatabase: RoomDatabase() {
    abstract fun widgetSectionDao(): WidgetSectionDao
}