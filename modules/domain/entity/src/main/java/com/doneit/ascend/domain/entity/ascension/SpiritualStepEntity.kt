package com.doneit.ascend.domain.entity.ascension

import java.util.*

class SpiritualStepEntity (
    id: Long,
    date: Date,
    val title: String,
    val repeat: String,
    val timeCommitment: String
): AscensionEntity(id, date)