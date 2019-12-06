package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "users")
data class UserLocal(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var id: Long = Random.nextLong(),
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val rating: Int = -1,
    val role: String? = "",
    val community: String? = ""

)