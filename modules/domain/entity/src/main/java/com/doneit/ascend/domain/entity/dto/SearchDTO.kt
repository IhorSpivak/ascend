package com.doneit.ascend.domain.entity.dto

class SearchDTO(
    val page: Int? = null,
    val perPage: Int? = null,
    val mmSortColumn: String? = null,
    val mmSortType: SortType? = null,
    val groupSortColumn: String? = null,
    val groupSortType: SortType? = null,
    val query: String? = null
)