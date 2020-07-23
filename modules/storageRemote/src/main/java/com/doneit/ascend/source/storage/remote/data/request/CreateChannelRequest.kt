package com.doneit.ascend.source.storage.remote.data.request

data class CreateChannelRequest(
    val title: String,
    val image: String?,
    val description: String?,
    val isPrivate: Boolean,
    val invites: List<Long>?
)