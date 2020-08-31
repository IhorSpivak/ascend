package com.doneit.ascend.presentation.main.filter.base_filter

import com.doneit.ascend.presentation.main.filter.FilterModel
import com.doneit.ascend.presentation.main.filter.FilterViewModel

class BaseFilterViewModel : FilterViewModel<FilterModel>() {
    override fun initFilterModel(): FilterModel {
        return FilterModel(
            mutableListOf(),
            System.currentTimeMillis(),
            System.currentTimeMillis()
        )
    }
}