package com.doneit.ascend.domain.entity.dto

class NotificationListDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val notificationType: String? = null,
    val createdAtFrom: String? = null,
    val createdAtTo: String? = null,
    val updatedAtFrom: String? = null,
    val updatedAtTo: String? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)