package com.doneit.ascend.presentation.main.home.community_feed.channels.common

import android.view.ViewGroup
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.common.PaginationAdapter
import com.doneit.ascend.presentation.main.chats.common.ChatDiffCallback

class ChannelAdapter(
    private val onChannelClick: (channel: ChatEntity) -> Unit
) : PaginationAdapter<ChatEntity, ChannelViewHolder>(ChatDiffCallback(), 0) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder.create(parent, onChannelClick)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}