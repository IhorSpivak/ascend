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
    val location: String? = "",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val meetingStarted: Boolean?,
    val newGroups: Boolean?,
    val inviteToMeeting: Boolean?,
    val displayName: String?,
    val description: String?,
    val bio: String?,
    val rating: Float? = -1f,
    val role: String?,
    val isMasterMind: Boolean,
    val community: String? = "",
    val visitedGroupCount: Int = 0,
    val birthday: String? = "",
    val imageURL: String? = "",//todo move to another table
    val thumbURL: String? = ""
)