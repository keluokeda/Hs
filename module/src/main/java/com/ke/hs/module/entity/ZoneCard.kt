package com.ke.hs.module.entity


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ZoneCard(
    val card: Card?,
    val entityId: Int,
    val position: Int
) : Parcelable