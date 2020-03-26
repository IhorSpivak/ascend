package com.doneit.ascend.presentation.main.create_group.calendar_picker

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface CalendarPickerContract {
    interface ViewModel : BaseViewModel {
        val canOk: LiveData<Boolean>
        val createGroupModel: PresentationCreateGroupModel

        fun backDateClick()
        fun backClick()
        fun okClick(hours: String, minutes: String, timeType: String)
        fun setHours(hours: String, timeType: String)
        fun setMinutes(minutes: String)
        fun setTimeType(timeType: String)
        fun changeDayState(day: CalendarDayEntity, state: Boolean)
    }
}