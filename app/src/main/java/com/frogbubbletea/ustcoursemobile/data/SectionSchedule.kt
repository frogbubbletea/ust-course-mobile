package com.frogbubbletea.ustcoursemobile.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SectionSchedule(
    val effectivePeriod: String,  // 24-OCT-2023 - 28-NOV-2023
    val dateTimes: String,  // Mo 01:30PM - 02:50PM
    val venue: String,  // "Rm 2464, Lift 25-26 (122)"
    val instructors: List<String>,  // ["CHAN, Gary Shueng Han", "CHEN, Hao", "CHEN, Long"]
    val teachingAssistants: List<String>,  // ["LAU, Hing Sang", "CHENG, Kei Tsi Daniel"]
): Parcelable
