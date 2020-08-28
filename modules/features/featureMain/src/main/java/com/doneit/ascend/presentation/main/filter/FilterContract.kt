package com.doneit.ascend.presentation.main.filter

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface FilterContract {
    interface ViewModel<T : FilterModel> : BaseViewModel {
        val filter: T
        val dataSource: List<String>

        fun selectDay(day: DayOfWeek)
        fun selectStartDate(startDate: Long)
        fun selectEndDate(endDate: Long)
        fun setFilter(filter: T)
    }
}