package com.frogbubbletea.ustcoursemobile.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservedQuota(
    val dept: String,  // CSE
    val quota: Int,  // 50
    val enrol: Int,  // 30
    val avail: Int,  // 20
): Parcelable
