package com.doneit.ascend.presentation.main.filter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FilterModel(
    open val selectedDays: MutableList<DayOfWeek> = mutableListOf(),
    open var timeFrom: Long = 0,
    open var timeTo: Long = 0
) : Parcelable