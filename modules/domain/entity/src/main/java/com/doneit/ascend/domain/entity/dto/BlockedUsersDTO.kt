package com.doneit.ascend.domain.entity.dto

class BlockedUsersDTO (
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val fullName: String? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)