package com.doneit.ascend.presentation.main.filter.tag_filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.presentation.main.filter.FilterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseTagFilterViewModel<T : TagFilterModel>(
    private val groupUseCase: GroupUseCase
) : FilterViewModel<T>(), TagFilterAbstractContract.ViewModel<T> {

    override val tags = MutableLiveData<List<TagEntity>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedTags = groupUseCase.getTags()
            if (loadedTags.isSuccessful) {
                tags.postValue(loadedTags.successModel.orEmpty())
            }
        }
    }

    override fun clearTags() {
        filter.selectedTagId = null
    }

    override fun tagSelected(tag: TagEntity) {
        if (filter.selectedTagId != tag.id) {
            filter.selectedTagId = tag.id
        } else {
            filter.selectedTagId = null
        }
    }
}