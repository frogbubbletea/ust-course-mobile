package com.frogbubbletea.ustcoursemobile.data.glanceData

import androidx.lifecycle.ViewModel
import androidx.room.PrimaryKey
import com.frogbubbletea.ustcoursemobile.data.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WidgetSectionViewModel @Inject constructor(
    private val widgetSectionRepository: WidgetSectionRepository
): ViewModel() {
    fun getWidgetSections(): Flow<List<WidgetSectionEntity>> = widgetSectionRepository.getAllSectionsStream()

    suspend fun setWidgetCourse(
        sections: List<Section>
    ) {
        widgetSectionRepository.deleteAllSections()
        sections.forEach {section ->
            widgetSectionRepository.insertSection(
                WidgetSectionEntity(
                    classNbr = section.classNbr,
                    sectionCode = section.code,
                    quota = section.totalQuota.quota,
                    enrol = section.totalQuota.enrol,
                    avail = section.totalQuota.avail,
                    wait = section.totalQuota.wait,
                    hasReserved = section.reservedQuotas.isEmpty()
                )
            )
        }
    }
}