package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel
import java.util.*

interface WebinarCalendarPickerContact {
    interface ViewModel : BaseViewModel {
        val canTimeChooserOk: LiveData<Boolean>
        val createGroupModel: PresentationCreateGroupModel

        fun backDateClick()
        fun backClick()
        fun okClick(hours: String, hourOfDay: String, minutes: String, timeType: String)
        fun okWebinarTimeClick(selectedDay: Int, date: Calendar, position: Int)
        fun setHours(hours: String, hourOfDay: String)
        fun setMinutes(minutes: String)
        fun setTimeType(timeType: String)
        fun changeDayState(day: CalendarDayEntity, state: Boolean)
        fun updateTimeChooserOk(state: Boolean)
    }
}