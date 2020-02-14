package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.AttachmentType

data class CreateAttachmentModel(
    var groupId: Long,
    val attachmentType: AttachmentType,
    val name: ValidatableField = ValidatableField(),
    val link: ValidatableField = ValidatableField(),
    var isPrivate: Boolean
)