package com.doneit.ascend.source.storage.local.data

import androidx.room.PrimaryKey

data class AttendeeLocal (
    @PrimaryKey
    var id: Long,
    val fullName: String = "",
    val email: String = "",
    val image: String? = ""
)