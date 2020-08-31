package com.doneit.ascend.presentation.main.filter

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.minutesToMills
import com.doneit.ascend.presentation.utils.extensions.toDayTime
import java.util.*

abstract class FilterViewModel<T : FilterModel> : BaseViewModelImpl(), FilterContract.ViewModel<T> {

    override val filter: T by lazy { initFilterModel() }

    override val dataSource: List<String> = List(INTERVALS_COUNT) {
        Date(it * TIME_INTERVAL.minutesToMills()).toDayTime()
    }

    override fun selectDay(day: DayOfWeek) {
        if (filter.selectedDays.contains(day)) {
            filter.selectedDays.remove(day)
        } else filter.selectedDays.add(day)
    }

    override fun selectStartDate(startDate: Long) {
        filter.timeFrom = startDate
    }

    override fun selectEndDate(endDate: Long) {
        filter.timeTo = endDate
    }

    override fun setFilter(filter: T) {
        this.filter.selectedDays.addAll(filter.selectedDays)
        this.filter.timeFrom = filter.timeFrom
        this.filter.timeTo = filter.timeTo
    }

    protected abstract fun initFilterModel(): T

    companion object {
        private const val TIME_INTERVAL = 5//5 min
        private const val INTERVALS_COUNT = 24 * 60 / TIME_INTERVAL//1 day

    }

}