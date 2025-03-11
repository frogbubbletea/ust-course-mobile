package com.frogbubbletea.usthong.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val prefix: Prefix,  // COMP
    val code: String,  // 2012H
    val semester: Semester,  // Semester("2024-25 Spring", 2430)
    val title: String,  // Honors Object-Oriented Programming and Data Structures
    val units: Int,  // 5
    val matching: MatchingRequirement,  // [Matching between Lecture & Tutorial required]
    val attributes: Map<String, String>,  // { "4Y": "Before 2022", "CC22": "From 2022" }
    val info: Map<String, String>,  // { "Pre-requisite": "Apple", "Exclusion": "Orange" }
    val sections: List<Section>
): Parcelable
