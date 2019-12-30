package com.doneit.ascend.presentation.profile.mm_followed.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.MasterMindEntity

class FollowedAdapter(
    private val unfollow: (Long)->Unit
) : PagedListAdapter<MasterMindEntity, FollowedViewHolder>(FollowedDiffCallback()) {

    init {
        setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowedViewHolder {
        return FollowedViewHolder.create(parent)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }

    override fun onBindViewHolder(holder: FollowedViewHolder, position: Int) {
        holder.bind(getItem(position)!!, unfollow)
    }
}