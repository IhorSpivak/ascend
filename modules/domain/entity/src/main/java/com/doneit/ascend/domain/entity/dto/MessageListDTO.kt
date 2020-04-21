package com.doneit.ascend.domain.entity.dto

import com.doneit.ascend.domain.entity.chats.MessageStatus

class MessageListDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val message: String? = null,
    val status: MessageStatus? = null,
    val userId: String? = null,
    val edited: Boolean? = null,
    val createdAtFrom: String? = null,
    val createdAtTo: String? = null,
    val updatedAtFrom: String? = null,
    val updatedAtTo: String? = null
): BasePagedDTO(page, perPage, sortColumn, sortType)