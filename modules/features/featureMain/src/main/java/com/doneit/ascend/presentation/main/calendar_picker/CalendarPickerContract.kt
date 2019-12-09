package com.doneit.ascend.presentation.main.calendar_picker

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CalendarPickerContract {
    interface ViewModel : BaseViewModel {
        fun backClick()
        fun okClick()
        fun setHours(hours: String)
        fun setMinutes(minutes: String)
    }
}