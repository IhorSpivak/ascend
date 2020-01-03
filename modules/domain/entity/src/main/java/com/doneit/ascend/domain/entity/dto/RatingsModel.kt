package com.doneit.ascend.domain.entity.dto

import java.util.*

class RatingsModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val fullName: String? = null,
    val createdAtFrom: Date? = null,
    val createdAtTo: Date? = null,
    val updatedAtFrom: Date? = null,
    val updatedAtTo: Date? = null
): BasePagedModel(page, perPage, sortColumn, sortType)