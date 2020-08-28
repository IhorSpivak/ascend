package com.doneit.ascend.presentation.main.filter

interface FilterListener<T : FilterModel> {
    fun updateFilter(filter: T)
}