package com.frogbubbletea.usthong.data

data class SectionSchedule(
    val effectivePeriod: String,  // 24-OCT-2023 - 28-NOV-2023
    val dateTimes: List<String>,  // ["Mo 01:30PM - 02:50PM", "Fr 09:00AM - 10:20AM"]
    val venue: String,  // G003, LSK Bldg (70)
    val instructors: List<String>,  // ["CHAN, Gary Shueng Han", "CHEN, Hao", "CHEN, Long"]
    val teachingAssistants: List<String>,  // ["LAU, Hing Sang", "CHENG, Kei Tsi Daniel"]
)
