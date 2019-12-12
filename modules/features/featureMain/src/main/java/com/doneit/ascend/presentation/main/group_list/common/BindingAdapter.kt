package com.doneit.ascend.presentation.main.group_list.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.GroupEntity

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupListAdapter,
    groups: LiveData<List<GroupEntity>>
) {

    if (view.adapter is GroupListAdapter) {
        groups.value?.let {
            (view.adapter as GroupListAdapter).updateData(it)
        }

        return
    }

    view.adapter = adapter
}