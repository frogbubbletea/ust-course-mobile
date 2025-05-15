package com.frogbubbletea.ustcoursemobile.data

sealed interface StarredCourseEvent {
    data class StarCourse(val course: CourseEntity): StarredCourseEvent
    data class UnstarCourse(val course: CourseEntity): StarredCourseEvent
}