package com.doneit.ascend.presentation.main.models

import androidx.databinding.ObservableField
import java.sql.Date

class PresentationCreateGroupModel(
    var name: ValidatableField = ValidatableField(),
    var schedule: ValidatableField = ValidatableField(),
    var numberOfMeetings: ValidatableField = ValidatableField(),
    var startDate: ValidatableField = ValidatableField(),
    var price: ValidatableField = ValidatableField(),
    var description: ValidatableField = ValidatableField(),
    var image: ObservableField<Any> = ObservableField(),
    var participants: ObservableField<List<String>> = ObservableField(),
    var groupType: String = "",
    var hours: String = "",
    var minutes: String = ""
)
