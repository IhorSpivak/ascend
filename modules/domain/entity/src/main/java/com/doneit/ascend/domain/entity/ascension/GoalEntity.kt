package com.doneit.ascend.domain.entity.ascension

import java.util.*

class GoalEntity (
    id: Long,
    date: Date,
    val title: String,
    val duration: Long,
    val repetion: Int,
    val goal: String
): AscensionEntity(id, date)