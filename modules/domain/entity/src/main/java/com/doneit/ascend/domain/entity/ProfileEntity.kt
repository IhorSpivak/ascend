package com.doneit.ascend.domain.entity

class ProfileEntity(
    val id: Long,
    val fullName: String?,
    val email: String?,
    val phone: String?,
    val location: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val meetingStarted: Boolean?,
    val newGroups: Boolean?,
    val inviteToMeeting: Boolean?,
    val unansweredQuestions: List<Int>?,
    val image: ImageEntity?,
    val displayName: String?,
    val description: String?,
    val bio: String?,
    val rating: Float?,
    val role: String?,
    val isMasterMind: Boolean,
    val community: String?
)