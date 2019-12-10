package com.doneit.ascend.presentation.main.models

import androidx.databinding.ObservableField

class PresentationCreateGroupModel(
    var name: ValidatableField = ValidatableField(),
    var schedule: ValidatableField = ValidatableField(),
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
    var scheduleDays: MutableList<String> = mutableListOf(),
    var scheduleTime: ValidatableField = ValidatableField()
)
