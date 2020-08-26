package com.doneit.ascend.domain.entity.dto

class ChatListDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val title: String? = null,
    val createdAtFrom: String? = null,
    val createdAtTo: String? = null,
    val updatedAtFrom: String? = null,
    val updatedAtTo: String? = null,
    val chatType: ChatType? = null,
    val allChannels: Boolean? = null,
    val ownerId: Long? = null,
    val community: String? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)