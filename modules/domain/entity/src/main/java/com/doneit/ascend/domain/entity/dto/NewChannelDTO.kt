package com.doneit.ascend.domain.entity.dto

data class NewChannelDTO(
    val title: String,
    val image: String?,
    val description: String?,
    val isPrivate: Boolean,
    val invites: List<Long>?
)
