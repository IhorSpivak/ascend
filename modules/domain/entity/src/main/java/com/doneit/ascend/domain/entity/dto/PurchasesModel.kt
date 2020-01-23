package com.doneit.ascend.domain.entity.dto

import java.util.*

class PurchasesModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val createdAtFrom: Date? = null,
    val createdAtTo: Date? = null
) : BasePagedModel(page, perPage, sortColumn, sortType)