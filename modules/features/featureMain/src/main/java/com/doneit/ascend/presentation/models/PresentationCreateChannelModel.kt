package com.doneit.ascend.presentation.models

import androidx.databinding.ObservableField

class PresentationCreateChannelModel(
    var title : ValidatableField = ValidatableField(),
    var image: ValidatableField = ValidatableField(),
    var description : ValidatableField = ValidatableField(),
    var isPrivate: ObservableField<Boolean> = ObservableField(false),
    var participants: ObservableField<List<Long>> = ObservableField()
)