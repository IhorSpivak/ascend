package com.doneit.ascend.domain.entity.dto

open class BasePagedDTO(
   val page: Int?,
   val perPage: Int?,
   val sortColumn: String?,
   val sortType: SortType?
)