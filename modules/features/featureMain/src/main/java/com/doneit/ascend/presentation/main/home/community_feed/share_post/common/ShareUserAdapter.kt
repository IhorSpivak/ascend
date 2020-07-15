package com.doneit.ascend.presentation.main.home.community_feed.share_post.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.chats.common.AttendeeDiffCallback

class ShareUserAdapter(
    private val onItemClick: (id: Long) -> Unit
) : PagedListAdapter<AttendeeEntity, ShareUserViewHolder>(AttendeeDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareUserViewHolder {
        return ShareUserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ShareUserViewHolder, position: Int) {
        holder.bind(getItem(position)!!, onItemClick)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!
    }
}