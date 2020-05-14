package com.doneit.ascend.domain.entity.dto

import java.util.*

data class UpdateGroupDTO(
    val name: String?,
    val description: String?,
    val startTime: Date?,
    val groupType: String?,
    val price: Float?,
    val imagePath: String?,
    val participants: List<String>?,
    val participantsToDelete: List<String>?,
    val days: List<Int>?,
    val meetingsCount: Int?,
    val meetingFormat: String?,
    val privacy: Boolean?,
    val tags: Int?,
    val times: List<String>?,
    val themes: List<String>?,
    val duration: Int?
)