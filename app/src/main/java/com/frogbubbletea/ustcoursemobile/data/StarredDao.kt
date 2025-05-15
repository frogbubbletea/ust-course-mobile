package com.frogbubbletea.ustcoursemobile.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StarredDao {
    @Query("SELECT * FROM courses ORDER BY prefixName ASC, code ASC, semCode DESC")
    fun getCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id")
    fun getCourse(id: String): Flow<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(course: CourseEntity)

    @Delete
    suspend fun delete(course: CourseEntity)
}