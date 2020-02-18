package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.AttachmentType

data class CreateAttachmentModel(
    var groupId: Long,
    val attachmentType: AttachmentType,
    val name: ValidatableField = ValidatableField(),
    val link: ValidatableField = ValidatableField(),
    var isPrivate: Boolean
)

data class CreateAttachmentFileModel(
    var groupId: Long,
    var attachmentType: AttachmentType,
    var name: String? = null,
    var link: String? = null,
    var size: Long? = null,
    var isPrivate: Boolean
)