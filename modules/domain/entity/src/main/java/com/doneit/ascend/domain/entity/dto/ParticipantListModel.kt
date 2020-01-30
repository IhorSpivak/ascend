package com.doneit.ascend.domain.entity.dto


class ParticipantListModel(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: SortType?,
    val fullName: String?,
    val connected: Boolean?,
    val groupId: Long
): BasePagedModel(page, perPage, sortColumn, sortType)