package com.doneit.ascend.source.storage.local.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocal(
    @PrimaryKey
    var id: Long,
    val fullName: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val location: String? = "",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val meetingStarted: Boolean?,
    val newGroups: Boolean?,
    val inviteToMeeting: Boolean?,
    val registrationType: String,
    val unansweredQuestionsCount: Int,
    val unreadNotificationsCount: Int,
    val blockedUsersCount: Int,
    @Embedded(prefix = "img") val image: ImageLocal?,
    val displayName: String?,
    val description: String?,
    val bio: String?,
    val rating: Float? = -1f,
    val isMasterMind: Boolean,
    val visitedGroupCount: Int = 0,
    val community: String? = "",
    val birthday: String? = "",
    val communities: List<String>?
)