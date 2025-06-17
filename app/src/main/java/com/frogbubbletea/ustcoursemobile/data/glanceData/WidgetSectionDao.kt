package com.frogbubbletea.ustcoursemobile.data.glanceData

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frogbubbletea.ustcoursemobile.data.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetSectionDao {
    @Query("SELECT * FROM widget_sections")
    fun getAllSections(): Flow<List<WidgetSectionEntity>>

    @Query("SELECT * FROM widget_sections WHERE classNbr = :classNbr")
    fun getSection(classNbr: Int): Flow<WidgetSectionEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(section: WidgetSectionEntity)

    @Delete
    suspend fun delete(section: WidgetSectionEntity)

    @Query("DELETE FROM widget_sections")
    suspend fun deleteAll()
}