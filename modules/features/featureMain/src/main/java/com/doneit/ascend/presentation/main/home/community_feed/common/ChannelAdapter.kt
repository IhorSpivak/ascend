package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.ViewGroup
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.common.PaginationAdapter
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.ChannelDiffUtilCallback

class ChannelAdapter(
    private val onChannelClick: (ChatEntity) -> Unit
) : PaginationAdapter<ChatEntity, ChannelViewHolder>(ChannelDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder.create(parent, onChannelClick)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}