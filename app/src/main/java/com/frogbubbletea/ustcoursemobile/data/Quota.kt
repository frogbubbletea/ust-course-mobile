package com.frogbubbletea.ustcoursemobile.data

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Quota(
    val quota: Int,  // 100
    val enrol: Int,  // 60
    val avail: Int, // 40
    val wait: Int,  // 0
): Parcelable
