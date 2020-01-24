package com.doneit.ascend.domain.entity.dto

import java.util.*

data class UpdateProfileModel(
    var fullName: String? = null,
    var displayName: String? = null,
    var location: String? = null,
    var isMeetingStarted: Boolean? = null,
    var hasNewGroups: Boolean? = null,
    var hasInviteToMeeting: Boolean? = null,
    var birthday: Date? = null,
    var bio: String? = null,
    var description: String? = null,
    var removeImage: Boolean? = null,
    var imagePath: String? = null
)