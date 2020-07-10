package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.ViewGroup
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.presentation.common.PaginationAdapter
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.ChannelDiffUtilCallback

class ChannelAdapter(
    private val onChannelClick: (Channel) -> Unit
) : PaginationAdapter<Channel, ChannelViewHolder>(ChannelDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder.create(parent, onChannelClick)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}