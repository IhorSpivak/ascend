package com.doneit.ascend.domain.entity.dto

import java.util.*

class PurchasesDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val createdAtFrom: Date? = null,
    val createdAtTo: Date? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)