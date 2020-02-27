package com.doneit.ascend.presentation.models.ascension_plan

import com.doneit.ascend.domain.entity.dto.ascension_plan.StepsDTO

data class PresentationAscensionFilter(
    var dateRange: PresentationDateRange,
    var steps: StepsDTO
)