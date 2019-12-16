package com.doneit.ascend.presentation.main.search.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.group_list.common.GroupDiffCallback

class SearchGroupsAdapter : PagedListAdapter<GroupEntity, SearchGroupViewHolder>(GroupDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchGroupViewHolder {
        return SearchGroupViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SearchGroupViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }
}