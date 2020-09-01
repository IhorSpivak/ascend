package com.doneit.ascend.domain.entity.dto

open class BasePagedDTO(
   open val page: Int?,
   open val perPage: Int?,
   open val sortColumn: String?,
   open val sortType: SortType?
)