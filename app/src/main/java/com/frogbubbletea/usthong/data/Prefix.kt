package com.frogbubbletea.usthong.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Prefix(
    val name: String,
    val type: PrefixType
): Parcelable
