package com.doneit.ascend.presentation.main.calendar_picker

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.utils.CalendarDay

interface CalendarPickerContract {
    interface ViewModel : BaseViewModel {
        val canOk: LiveData<Boolean>

        fun backClick()
        fun okClick()
        fun setHours(hours: String)
        fun setMinutes(minutes: String)
        fun setTimeType(timeType: String)
        fun changeDayState(day: CalendarDay, state: Boolean)
    }
}