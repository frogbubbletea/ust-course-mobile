package com.frogbubbletea.ustcoursemobile.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: String,
    val prefixName: String,
    val prefixType: String,
    val code: String,
    val semCode: Int,
    val semName: String
)