package com.doneit.ascend.domain.entity.dto

data class UpdateProfileModel(
    var fullName: String?,
    var displayName: String?,
    var location: String?,
    var isMeetingStarted: Boolean?,
    var hasNewGroups: Boolean?,
    var hasInviteToMeeting: Boolean?,
    var age: Int?,
    var bio: String?,
    var description: String?,
    var shouldUpdateIcon: Boolean,
    var imagePath: String?
)