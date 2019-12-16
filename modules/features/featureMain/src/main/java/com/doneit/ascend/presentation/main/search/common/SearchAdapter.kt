package com.doneit.ascend.presentation.main.search.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.presentation.main.group_list.common.GroupHorViewHolder
import com.doneit.ascend.presentation.main.master_mind.list.common.MasterMindViewHolder

class SearchAdapter : PagedListAdapter<SearchEntity, SearchViewHolder>(SearchDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is GroupEntity -> 0
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return if(viewType == 0) {
            GroupHorViewHolder.create(parent)
        } else {
            MasterMindViewHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        when(val model = getItem(position)) {
            is GroupEntity -> {
                (holder as GroupHorViewHolder).bind(model, null)
            }
            is MasterMindEntity -> {
                (holder as MasterMindViewHolder).bind(model)
            }
        }
    }
}