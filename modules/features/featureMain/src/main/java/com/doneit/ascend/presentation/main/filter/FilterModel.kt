package com.doneit.ascend.presentation.main.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FilterModel(
    val selectedDays: MutableList<DayOfWeek>,
    var timeFrom: Long,
    var timeTo: Long
) : Parcelable