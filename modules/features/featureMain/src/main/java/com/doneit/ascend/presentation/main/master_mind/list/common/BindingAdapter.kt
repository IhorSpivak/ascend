package com.doneit.ascend.presentation.main.master_mind.list.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MasterMindEntity

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: MasterMindAdapter,
    groups: LiveData<PagedList<MasterMindEntity>>
) {

    if (view.adapter is MasterMindAdapter) {
        groups.value?.let {
            (view.adapter as MasterMindAdapter).submitList(it)
        }

        return
    }

    view.adapter = adapter
}