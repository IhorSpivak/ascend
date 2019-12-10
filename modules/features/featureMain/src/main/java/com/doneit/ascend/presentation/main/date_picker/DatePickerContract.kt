package com.doneit.ascend.presentation.main.date_picker

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface DatePickerContract {
    interface ViewModel : BaseViewModel {
        fun cancelClick()
        fun doneClick()

        fun setMonth(month: Int)
        fun setDay(day: Int)
        fun setYear(year: Int)
    }
}