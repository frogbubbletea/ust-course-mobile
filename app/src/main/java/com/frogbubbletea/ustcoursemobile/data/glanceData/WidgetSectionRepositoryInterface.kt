package com.frogbubbletea.ustcoursemobile.data.glanceData

import kotlinx.coroutines.flow.Flow

interface WidgetSectionRepositoryInterface {
    fun getAllSectionsStream(): Flow<List<WidgetSectionEntity>>

    fun getSectionStream(classNbr: Int): Flow<WidgetSectionEntity>

    suspend fun insertSection(section: WidgetSectionEntity)

    suspend fun deleteSection(section: WidgetSectionEntity)

    suspend fun insertListOfSections(sections: List<WidgetSectionEntity>)

    suspend fun deleteListOfSections(sections: List<WidgetSectionEntity>)

    suspend fun deleteAllSections()
}