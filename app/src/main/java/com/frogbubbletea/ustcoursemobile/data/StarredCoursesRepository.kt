package com.frogbubbletea.ustcoursemobile.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StarredCoursesRepository @Inject constructor (
    private val starredDao: StarredDao
): StarredRepository {
    override fun getAllCoursesStream(): Flow<List<CourseEntity>> = starredDao.getCourses()

    override fun getCourseStream(id: String): Flow<CourseEntity?> = starredDao.getCourse(id)

    override suspend fun insertCourse(course: CourseEntity) = starredDao.insert(course)

    override suspend fun deleteCourse(course: CourseEntity) = starredDao.delete(course)
}