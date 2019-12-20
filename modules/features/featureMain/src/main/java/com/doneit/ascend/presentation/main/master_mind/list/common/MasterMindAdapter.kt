package com.doneit.ascend.presentation.main.master_mind.list.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.MasterMindEntity

class MasterMindAdapter(
    private val onItemClick:(id: Long)->Unit,
    private val onSeeGroupsClick: (id: Long)->Unit
) :
    PagedListAdapter<MasterMindEntity, MasterMindViewHolder>(MasterMindDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterMindViewHolder {
        return MasterMindViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MasterMindViewHolder, position: Int) {
        holder.bind(getItem(position)!!, onSeeGroupsClick)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(getItem(position)!!.id)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }
}