package com.doneit.ascend.presentation.profile.mm_following.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.MasterMindEntity

class FollowingAdapter(
    private val openInfo: (MasterMindEntity) -> Unit,
    private val follow: (Long)->Unit = {},
    private val unfollow: (Long)->Unit = {}
) : PagedListAdapter<MasterMindEntity, FollowingViewHolder>(FollowingDiffCallback()) {

    init {
        setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        return FollowingViewHolder.create(parent)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(getItem(position)!!, {
            follow.invoke(it)
        }, {
            unfollow.invoke(it)
        }, openInfo)
    }
}