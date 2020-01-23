package com.doneit.ascend.domain.entity.dto

class PurchasesModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val createdAtFrom: String?,
    val createdAtTo: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)