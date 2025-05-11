package com.frogbubbletea.ustcoursemobile.data

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Prefix(
    val name: String,
    val type: PrefixType
): Parcelable
