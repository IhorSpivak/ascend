package com.doneit.ascend.domain.entity.dto

class MasterMindListDTO (
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val fullName: String? = null,
    val displayName: String? = null,
    val followed: Boolean? = null,
    val rated: Boolean? = null
): BasePagedDTO(page, perPage, sortColumn, sortType)