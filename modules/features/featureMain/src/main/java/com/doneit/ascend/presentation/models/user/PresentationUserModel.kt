package com.doneit.ascend.presentation.models.user

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import java.util.*

data class PresentationUserModel(
    val id: Long,
    val fullName: String?,
    val email: String?,
    val phone: String?,
    val location: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val meetingStarted: Boolean?,
    val newGroups: Boolean?,
    val inviteToMeeting: Boolean?,
    val registrationType: PresentationRegistrationType,
    val unansweredQuestionsCount: Int,
    val unreadNotificationsCount: Int,
    val image: ImageEntity?,
    val displayName: String?,
    val description: String?,
    val bio: String?,
    val rating: Float?,
    val isMasterMind: Boolean,
    val community: String?,
    val visitedGroupCount: Int,
    val birthday: Date?
) {
    val age: Int?
        get() {
            return if (birthday == null) null else UserEntity.getAge(
                birthday
            )
        }
}