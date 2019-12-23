package com.doneit.ascend.domain.entity.dto

import java.util.*

data class CreateGroupModel(
    val name: String,
    val description: String,
    val startTime: Date,
    val groupType: String,
    val price: String,
    val imagePath: String,
    val participants: List<String>?,
    val days: List<Int>,
    val meetingsCount: Int
)