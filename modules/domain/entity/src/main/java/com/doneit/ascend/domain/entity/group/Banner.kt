package com.doneit.ascend.domain.entity.group

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Banner(
    val title: String,
    val bannerType: String
) : Parcelable