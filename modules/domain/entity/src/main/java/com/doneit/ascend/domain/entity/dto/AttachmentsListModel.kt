package com.doneit.ascend.domain.entity.dto

import java.util.*

class AttachmentsListModel(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: SortType?,
    val groupId: Long?,
    val userId: Long?,
    val fileName: String?,
    val link: String?,
    val private: Boolean?,
    val createdAtFrom: Date?,
    val createdAtTo: Date?,
    val updatedAtFrom: Date?,
    val updatedAtTo: Date?
) : BasePagedModel(page, perPage, sortColumn, sortType)