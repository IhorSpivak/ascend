package com.doneit.ascend.presentation.main.group_list.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

class GroupListAdapter(
    private var user: UserEntity? = null
) : PagedListAdapter<GroupEntity, GroupHorViewHolder>(GroupDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHorViewHolder {
        return GroupHorViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GroupHorViewHolder, position: Int) {
        holder.bind(getItem(position)!!, user)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }

    fun setUser(user: UserEntity) {
        this.user = user
    }
}