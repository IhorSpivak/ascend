package com.doneit.ascend.presentation.models.ascension_plan

import androidx.databinding.ObservableField

data class PresentationSpiritualStepModel(
    val title: ObservableField<String> = ObservableField(),
    val repeat: ObservableField<String> = ObservableField(),
    val timeCommitment: ObservableField<String> = ObservableField()
)