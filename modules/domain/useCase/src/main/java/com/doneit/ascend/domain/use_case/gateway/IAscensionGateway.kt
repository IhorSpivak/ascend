package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ascension.AscensionEntity
import com.doneit.ascend.domain.entity.dto.ascension_plan.FilterDTO

interface IAscensionGateway {
    fun getListPaged(filter: FilterDTO): LiveData<PagedList<AscensionEntity>>
}