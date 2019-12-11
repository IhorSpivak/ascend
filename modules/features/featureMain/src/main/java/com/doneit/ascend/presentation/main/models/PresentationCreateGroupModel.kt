package com.doneit.ascend.presentation.main.models

import androidx.databinding.ObservableField
import com.doneit.ascend.presentation.utils.CalendarDay

class PresentationCreateGroupModel(
    var name: ValidatableField = ValidatableField(),
    var numberOfMeetings: ValidatableField = ValidatableField(),
    var startDate: ValidatableField = ValidatableField(),
    var price: ValidatableField = ValidatableField(),
    var description: ValidatableField = ValidatableField(),
    var image: ValidatableField = ValidatableField(),
    var participants: ObservableField<List<String>> = ObservableField(),
    var groupType: String = "",
    var hours: String = "",
    var minutes: String = "",
    var timeType: String = "",
    var month: Int = 0,
    var day: Int = 0,
    var year: Int = 0,
    var selectedDays: MutableList<CalendarDay> = mutableListOf(),
    var scheduleDays: MutableList<CalendarDay> = mutableListOf(),
    var scheduleTime: ValidatableField = ValidatableField()
)
