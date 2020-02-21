package com.doneit.ascend.presentation.main.search.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.SearchEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorViewHolder
import com.doneit.ascend.presentation.main.master_mind.list.common.MasterMindViewHolder

class SearchAdapter(
    private val onSeeGroupsClick: (id: Long)->Unit,
    private val onMMClick: (model: MasterMindEntity)->Unit,
    private val onGroupClick: (model: GroupEntity)->Unit,
    private val onButtonClick: (GroupEntity) -> Unit
) : PagedListAdapter<SearchEntity, SearchViewHolder>(SearchDiffCallback()) {

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

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        when(val model = getItem(position)) {
            is GroupEntity -> {
                (holder as GroupHorViewHolder).bind(model, null, onButtonClick)
                holder.itemView.setOnClickListener {
                    onGroupClick.invoke(model)
                }
            }
            is MasterMindEntity -> {
                (holder as MasterMindViewHolder).bind(model, onSeeGroupsClick)
                holder.itemView.setOnClickListener {
                    onMMClick.invoke(model)
                }
            }
        }
    }
}