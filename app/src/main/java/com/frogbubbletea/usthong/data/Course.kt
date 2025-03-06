package com.frogbubbletea.usthong.data

data class Course(
    val prefix: String,  // COMP
    val code: String,  // 2012H
    val title: String,  // Honors Object-Oriented Programming and Data Structures
    val units: Int,  // 5
    val matching: MatchingRequirement,  // [Matching between Lecture & Tutorial required]
    val attributes: Map<String, String>,  // { "4Y": "Before 2022", "CC22": "From 2022" }
    val info: Map<String, String>,  // { "Pre-requisite": "Apple", "Exclusion": "Orange" }
    val sections: List<Section>
)
