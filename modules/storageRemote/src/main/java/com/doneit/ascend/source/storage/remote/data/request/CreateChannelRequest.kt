package com.doneit.ascend.source.storage.remote.data.request

import java.io.File

data class CreateChannelRequest(
    val title: String,
    val image: File?,
    val description: String?,
    val isPrivate: Boolean,
    val invites: List<Long>?
)