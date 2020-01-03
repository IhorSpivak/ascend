package com.doneit.ascend.presentation.models

import androidx.databinding.ObservableField
import com.doneit.ascend.domain.entity.CalendarDayEntity
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.utils.CalendarPickerUtil

class PresentationCreateGroupModel(
    var name: ValidatableField = ValidatableField(),
    var numberOfMeetings: ValidatableField = ValidatableField(),
    var startDate: ValidatableField = ValidatableField(),
    var price: ValidatableField = ValidatableField(),
    var description: ValidatableField = ValidatableField(),
    var image: ValidatableField = ValidatableField(),
    var participants: ObservableField<List<String>> = ObservableField(),
    var groupType: String = "",
    var hours: String = "00",
    var hoursPosition: Int = 0,
    var minutes: String = "00",
    var minutesPosition: Int = 0,
    var timeType: String = CalendarPickerUtil.DEFAULT_TIME_TYPE,
    var timeTypePosition: Int = 0,
    var month: MonthEntity = MonthEntity.getActual(),
    var day: Int = 0,
    var dayPosition: Int = -1,
    var year: Int = 0,
    var yearPosition: Int = 0,
    var selectedDays: MutableList<CalendarDayEntity> = mutableListOf(),
    var scheduleDays: MutableList<CalendarDayEntity> = mutableListOf(),
    var scheduleTime: ValidatableField = ValidatableField()
)