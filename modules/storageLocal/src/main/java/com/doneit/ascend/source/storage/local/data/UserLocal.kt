package com.doneit.ascend.source.storage.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocal(
    @PrimaryKey
    var id: Long,
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val rating: Int = -1,
    val role: String? = "",
    val community: String? = ""

)