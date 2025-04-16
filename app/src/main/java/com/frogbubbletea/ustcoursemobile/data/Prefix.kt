package com.frogbubbletea.ustcoursemobile.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prefix(
    val name: String,
    val type: PrefixType
): Parcelable
