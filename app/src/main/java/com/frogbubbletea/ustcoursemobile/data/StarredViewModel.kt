package com.frogbubbletea.ustcoursemobile.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarredViewModel @Inject constructor(
    private val starredRepository: StarredRepository
): ViewModel() {
    //    val starredCourses = starredRepository.getAllCoursesStream()
    fun getStarredCourses(): Flow<List<CourseEntity>> = starredRepository.getAllCoursesStream()

    fun onEvent(event: StarredCourseEvent) {
        when (event) {
            is StarredCourseEvent.StarCourse -> {
                viewModelScope.launch {
                    starredRepository.insertCourse(event.course)
                }
            }
            is StarredCourseEvent.UnstarCourse -> {
                viewModelScope.launch {
                    starredRepository.deleteCourse(event.course)
                }
            }
        }
    }

    suspend fun starCourse(
        prefixName: String,
        prefixType: String,
        code: String,
        semCode: Int,
        semName: String
    ) {
        val starringCourse: CourseEntity = CourseEntity(
            id = prefixName + code + semCode.toString(),
            prefixName = prefixName,
            prefixType = prefixType,
            code = code,
            semCode = semCode,
            semName = semName
        )

        starredRepository.insertCourse(starringCourse)
    }

    suspend fun unstarCourse(
        prefixName: String,
        prefixType: String,
        code: String,
        semCode: Int,
        semName: String
    ) {
        val starringCourse: CourseEntity = CourseEntity(
            id = prefixName + code + semCode.toString(),
            prefixName = prefixName,
            prefixType = prefixType,
            code = code,
            semCode = semCode,
            semName = semName
        )

        starredRepository.deleteCourse(starringCourse)
    }
}