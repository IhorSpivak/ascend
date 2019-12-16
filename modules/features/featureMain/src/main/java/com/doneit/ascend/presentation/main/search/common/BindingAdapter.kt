package com.doneit.ascend.presentation.main.search.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.GroupEntity

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: SearchGroupsAdapter,
    groups: LiveData<PagedList<GroupEntity>>?
) {

    if (view.adapter is SearchGroupsAdapter) {
        groups?.value?.let {
            (view.adapter as SearchGroupsAdapter).submitList(it)

            return
        }
    }

    view.adapter = adapter
}