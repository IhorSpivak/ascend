package com.doneit.ascend.domain.entity.dto

data class UserSearchDTO(
    val page: Int? = null,
    val perPage: Int? = null,
    val sortColumn: String? = null,
    val sortType: SortType? = null,
    val fullName: String? = null,
    val email: String? = null
)