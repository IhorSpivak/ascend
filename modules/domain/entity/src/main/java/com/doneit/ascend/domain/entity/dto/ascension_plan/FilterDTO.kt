package com.doneit.ascend.domain.entity.dto.ascension_plan

import com.doneit.ascend.domain.entity.dto.BasePagedDTO
import com.doneit.ascend.domain.entity.dto.SortType

class FilterDTO (
    page: Int? = null,
    perPage: Int? = null,
    sortColumn: String? = null,
    sortType: SortType? = null,
    var dateRange: DateRangeDTO,
    var steps: StepsDTO
): BasePagedDTO(page, perPage, sortColumn, sortType)