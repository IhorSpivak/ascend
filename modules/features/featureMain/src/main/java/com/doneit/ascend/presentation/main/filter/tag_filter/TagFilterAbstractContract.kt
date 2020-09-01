package com.doneit.ascend.presentation.main.filter.tag_filter

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.presentation.main.filter.FilterContract

interface TagFilterAbstractContract {
    interface ViewModel<T : TagFilterModel> : FilterContract.ViewModel<T> {
        val tags: LiveData<List<TagEntity>>

        fun tagSelected(tag: TagEntity)
    }
}

interface TagFilterContract {
    interface ViewModel : TagFilterAbstractContract.ViewModel<TagFilterModel>
}