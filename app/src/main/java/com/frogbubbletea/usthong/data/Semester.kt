package com.frogbubbletea.usthong.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Semester(
    val name: String,
    val code: Int
): Parcelable
