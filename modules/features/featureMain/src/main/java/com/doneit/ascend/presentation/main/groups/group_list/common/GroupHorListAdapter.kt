package com.doneit.ascend.presentation.main.groups.group_list.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity

class GroupHorListAdapter(
    private var user: UserEntity? = null,
    private val onItemClick: (model: GroupEntity) -> Unit,
    private val onButtonClick: (GroupEntity) -> Unit,
    private var community: String?
) : PagedListAdapter<GroupEntity, GroupHorViewHolder>(GroupDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHorViewHolder {
        return GroupHorViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GroupHorViewHolder, position: Int) {
        holder.bind(getItem(position)!!, user, onButtonClick, community)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(getItem(position)!!)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }

    fun setUser(user: UserEntity) {
        this.user = user
        notifyDataSetChanged()
    }

    fun setCommunity(community: String) {
        this.community = community
        notifyDataSetChanged()
    }
}