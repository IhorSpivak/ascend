package com.doneit.ascend.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class SearchEntity(
    override var id: Long
): IdentifiableEntity(id), Parcelable