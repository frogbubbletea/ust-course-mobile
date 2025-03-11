package com.frogbubbletea.usthong.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class Semester(
    val name: String,
    val code: Int
): Parcelable

// Construct semester code from
fun semesterCodeToInstance(
    code: Int
): Semester {
    val yearCode: String = if (code / 100 < 9) {
        "0" + (code / 100).toString()
    } else {
        (code / 100).toString()
    }

    val nextYearCode: String = if ((code / 100) + 1 < 9) {
        "0" + ((code / 100) + 1).toString()
    } else {
        ((code / 100) + 1).toString()
    }

    val semName: String = when (code % 100) {
        10 -> "Fall"
        20 -> "Winter"
        30 -> "Spring"
        40 -> "Summer"
        else -> throw IllegalArgumentException("Semester code must end in 10, 20, 30, or 40.")
    }

    return Semester(
        name = "20${yearCode}-${nextYearCode} ${semName}",
        code = code
    )
}
