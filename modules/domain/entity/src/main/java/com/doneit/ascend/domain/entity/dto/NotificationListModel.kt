package com.doneit.ascend.domain.entity.dto

class NotificationListModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val notificationType: String? = null,
    val createdAtFrom: String? = null,
    val createdAtTo: String? = null,
    val updatedAtFrom: String? = null,
    val updatedAtTo: String? = null
) : BasePagedModel(page, perPage, sortColumn, sortType)