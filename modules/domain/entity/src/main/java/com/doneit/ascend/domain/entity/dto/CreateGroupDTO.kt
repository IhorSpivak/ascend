package com.doneit.ascend.domain.entity.dto

import java.util.*

data class CreateGroupDTO(
    val name: String,
    val description: String,
    val startTime: Date,
    val groupType: String,
    val price: Float?,
    val imagePath: String,
    val participants: List<String>?,
    val days: List<Int>,
    val meetingsCount: Int,
    val meetingFormat: String?,
    val privacy: Boolean?,
    val tags: String?
)