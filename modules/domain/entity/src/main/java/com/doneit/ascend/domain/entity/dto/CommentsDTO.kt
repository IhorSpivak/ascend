package com.doneit.ascend.domain.entity.dto

class CommentsDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val createdAtFrom: String? = null,
    val createdAtTo: String? = null,
    val updatedAtFrom: String? = null,
    val updatedAtTo: String? = null,
    val text: String? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)