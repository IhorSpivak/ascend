package com.doneit.ascend.domain.entity.dto

class GroupListModel(
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    val name: String? = null,
    val groupType: GroupType? = GroupType.MASTER_MIND
): BasePagedModel(page, perPage, sortColumn, sortType)