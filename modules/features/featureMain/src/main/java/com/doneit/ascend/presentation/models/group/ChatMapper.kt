package com.doneit.ascend.presentation.models.group

import com.doneit.ascend.domain.entity.dto.CreateChatDTO
import com.doneit.ascend.presentation.models.PresentationCreateChatModel

fun PresentationCreateChatModel.toDTO(): CreateChatDTO{
    return CreateChatDTO(
        members = chatMembers.map { it.toInt() }
    )
}