package com.doneit.ascend.domain.entity.dto

data class CreateGroupModel(
    val name: String,
    val description: String,
    val startTime: String,
    val groupType: String,
    val price: String,
    val imagePath: String,
    val participants: List<String>?,
    val days: List<Int>,
    val meetingsCount: Int
)