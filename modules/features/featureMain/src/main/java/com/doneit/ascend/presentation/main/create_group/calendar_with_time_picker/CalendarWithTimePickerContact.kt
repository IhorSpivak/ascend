package com.doneit.ascend.presentation.main.create_group.calendar_with_time_picker

import android.icu.text.TimeZoneFormat
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CalendarWithTimePickerContact {
    interface ViewModel : BaseViewModel {
        fun getMonthList(): List<MonthEntity>
        fun cancelClick()
        fun doneClick()
        fun backDateClick()
        fun setType(type: TimeZoneFormat.TimeType)
        fun getType()
        fun setMinute(minute: Short)
        fun getMinute()
        fun setHour(hour: Short)
        fun getHour()
        fun setMonth(month: MonthEntity)
        fun getMonth(): MonthEntity
        fun setDay(day: Int)
        fun getDay(): Int
        fun setYear(year: Int)
        fun getYear(): Int
    }
}