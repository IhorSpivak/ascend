package com.doneit.ascend.domain.entity.dto

class SearchModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val query: String? = null
) : BasePagedModel(page, perPage, sortColumn, sortType)