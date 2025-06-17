package com.frogbubbletea.ustcoursemobile.data.glanceData

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.frogbubbletea.ustcoursemobile.ui.glance.CourseWidget
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class WidgetSectionRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val widgetSectionDao: WidgetSectionDao
): WidgetSectionRepositoryInterface {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetSectionRepositoryEntryPoint {
        fun widgetSectionRepository(): WidgetSectionRepository
    }

    companion object {
        fun get(applicationContext: Context): WidgetSectionRepository {
            val widgetSectionRepositoryEntryPoint: WidgetSectionRepositoryEntryPoint =
                EntryPoints.get(
                    applicationContext,
                    WidgetSectionRepositoryEntryPoint::class.java,
                )
            return widgetSectionRepositoryEntryPoint.widgetSectionRepository()
        }
    }

    private suspend fun sectionsUpdated() {
        CourseWidget().updateAll(appContext)
    }

    fun loadSections(): Flow<List<WidgetSectionEntity>> {
        return widgetSectionDao.getAllSections().distinctUntilChanged()
    }

    override fun getAllSectionsStream(): Flow<List<WidgetSectionEntity>> = widgetSectionDao.getAllSections()

    override fun getSectionStream(classNbr: Int): Flow<WidgetSectionEntity> = widgetSectionDao.getSection(classNbr)

    override suspend fun insertSection(section: WidgetSectionEntity) {
        widgetSectionDao.insert(section)
        sectionsUpdated()
    }

    override suspend fun deleteSection(section: WidgetSectionEntity) {
        widgetSectionDao.delete(section)
        sectionsUpdated()
    }

    override suspend fun insertListOfSections(sections: List<WidgetSectionEntity>) {
        sections.forEach {section ->
            widgetSectionDao.insert(section)
        }
        sectionsUpdated()
    }

    override suspend fun deleteListOfSections(sections: List<WidgetSectionEntity>) {
        sections.forEach {section ->
            widgetSectionDao.delete(section)
        }
        sectionsUpdated()
    }

    override suspend fun deleteAllSections() {
        widgetSectionDao.deleteAll()
        sectionsUpdated()
    }
}