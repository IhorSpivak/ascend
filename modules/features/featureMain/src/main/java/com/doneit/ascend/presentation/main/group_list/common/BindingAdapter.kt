package com.doneit.ascend.presentation.main.group_list.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.model.GroupListWithUser

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupListAdapter,
    groups: LiveData<GroupListWithUser>
) {

    if (view.adapter is GroupListAdapter) {
        groups.value?.let {
            (view.adapter as GroupListAdapter).setUser(it.user)
            (view.adapter as GroupListAdapter).updateData(it.groups!!)
        }

        return
    }

    view.adapter = adapter
}