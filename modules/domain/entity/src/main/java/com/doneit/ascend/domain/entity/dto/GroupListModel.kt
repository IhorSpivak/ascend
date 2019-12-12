package com.doneit.ascend.domain.entity.dto

class GroupListModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val name: String? = null,
    val userId: Long? = null,
    val groupType: GroupType? = null,
    val myGroups: Boolean? = null
): BasePagedModel(page, perPage, sortColumn, sortType)