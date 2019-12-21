package com.doneit.ascend.presentation.main.date_picker

import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface DatePickerContract {
    interface ViewModel : BaseViewModel {
        fun getMonthList(): List<MonthEntity>
        fun cancelClick()
        fun doneClick()
        fun backDateClick()
        fun setMonth(month: MonthEntity)
        fun getMonth(): MonthEntity
        fun setDay(day: Int)
        fun setDayPosition(position: Int)
        fun getDayPosition(): Int
        fun setYear(year: Int)
        fun getYear(): Int
        fun setYearPosition(position: Int)
        fun getYearPosition(): Int
    }
}