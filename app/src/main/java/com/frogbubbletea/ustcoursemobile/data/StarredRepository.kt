package com.frogbubbletea.ustcoursemobile.data

import kotlinx.coroutines.flow.Flow

interface StarredRepository {
    fun getAllCoursesStream(): Flow<List<CourseEntity>>

    fun getCourseStream(id: String): Flow<CourseEntity?>

    suspend fun insertCourse(course: CourseEntity)

    suspend fun deleteCourse(course: CourseEntity)
}