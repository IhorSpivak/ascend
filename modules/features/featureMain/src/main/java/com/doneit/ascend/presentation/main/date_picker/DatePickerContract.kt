package com.doneit.ascend.presentation.main.date_picker

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface DatePickerContract {
    interface ViewModel : BaseViewModel {
        fun cancelClick()
        fun doneClick()
        fun backDateClick()
        fun setMonth(month: Int)
        fun getMonth(): Int
        fun setMonthPosition(position: Int)
        fun getMonthPosition(): Int
        fun setDay(day: Int)
        fun setDayPosition(position: Int)
        fun getDayPosition(): Int
        fun setYear(year: Int)
        fun getYear(): Int
        fun setYearPosition(position: Int)
        fun getYearPosition(): Int
    }
}