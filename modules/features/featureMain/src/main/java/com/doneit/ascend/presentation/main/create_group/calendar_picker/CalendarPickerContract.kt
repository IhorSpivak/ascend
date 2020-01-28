package com.doneit.ascend.presentation.main.create_group.calendar_picker

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.presentation.models.PresentationCreateGroupModel

interface CalendarPickerContract {
    interface ViewModel : BaseViewModel {
        val canOk: LiveData<Boolean>
        val createGroupModel: PresentationCreateGroupModel

        fun backDateClick()
        fun backClick()
        fun okClick()
        fun setHours(hours: String)
        fun getHoursPosition(): Int
        fun setHoursPosition(position: Int)
        fun setMinutes(minutes: String)
        fun getMinutesPosition(): Int
        fun setMinutesPosition(position: Int)
        fun setTimeType(timeType: String)
        fun getTimeType(): String
        fun getTimeTypePosition(): Int
        fun setTimeTypePosition(position: Int)
        fun changeDayState(day: CalendarDayEntity, state: Boolean)
    }
}