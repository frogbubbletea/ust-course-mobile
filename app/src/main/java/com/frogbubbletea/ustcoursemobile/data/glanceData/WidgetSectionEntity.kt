package com.frogbubbletea.ustcoursemobile.data.glanceData

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widget_sections")
data class WidgetSectionEntity(
    @PrimaryKey val classNbr: Int,
    val sectionCode: String,
    val quota: Int,
    val enrol: Int,
    val avail: Int,
    val wait: Int,
    val hasReserved: Boolean
)
