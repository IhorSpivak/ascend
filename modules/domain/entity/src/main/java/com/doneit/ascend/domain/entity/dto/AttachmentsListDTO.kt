package com.doneit.ascend.domain.entity.dto

import java.util.*

class AttachmentsListDTO(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val groupId: Long? = null,
    val userId: Long? = null,
    val fileName: String? = null,
    val link: String? = null,
    val private: Boolean? = null,
    val createdAtFrom: Date? = null,
    val createdAtTo: Date? = null,
    val updatedAtFrom: Date? = null,
    val updatedAtTo: Date? = null
) : BasePagedDTO(page, perPage, sortColumn, sortType)