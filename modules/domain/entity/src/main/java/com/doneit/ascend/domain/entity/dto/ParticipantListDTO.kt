package com.doneit.ascend.domain.entity.dto


class ParticipantListDTO(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: SortType?,
    val fullName: String?,
    val connected: Boolean?,
    val groupId: Long
): BasePagedDTO(page, perPage, sortColumn, sortType)