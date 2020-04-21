package com.doneit.ascend.domain.entity.dto

class MemberListDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val fullName: String? = null,
    val online: Boolean? = null
): BasePagedDTO(page, perPage, sortColumn, sortType)