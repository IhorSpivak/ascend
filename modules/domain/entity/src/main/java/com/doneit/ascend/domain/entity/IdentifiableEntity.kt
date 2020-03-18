package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class IdentifiableEntity (
    open val id: Long
): Parcelable