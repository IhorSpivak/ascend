package com.doneit.ascend.presentation.models.ascension_plan

import com.doneit.ascend.domain.entity.dto.ascension_plan.DateRangeDTO
import com.doneit.ascend.domain.entity.dto.ascension_plan.FilterDTO

fun PresentationAscensionFilter.toEntity(): FilterDTO {
    return FilterDTO(
        dateRange = dateRange.toEntity(),
        steps = steps
    )
}

fun PresentationDateRange.toEntity(): DateRangeDTO {
    return when (this) {
        PresentationDateRange.TODAY -> DateRangeDTO.TODAY
        PresentationDateRange.WEEKLY -> DateRangeDTO.WEEKLY
        PresentationDateRange.MONTHLY -> DateRangeDTO.MONTHLY
    }
}