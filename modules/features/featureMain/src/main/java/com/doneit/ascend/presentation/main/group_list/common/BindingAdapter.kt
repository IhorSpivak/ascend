package com.doneit.ascend.presentation.main.group_list.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.models.GroupListWithUser
import com.doneit.ascend.presentation.main.models.GroupListWithUserPaged

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView,
    adapter: GroupListAdapter,
    groups: LiveData<GroupListWithUserPaged>
) {

    if (view.adapter is GroupListAdapter) {
        groups.value?.let {
            (view.adapter as GroupListAdapter).setUser(it.user)
            (view.adapter as GroupListAdapter).submitList(it.groups!!)
        }

        return
    }

    view.adapter = adapter
}