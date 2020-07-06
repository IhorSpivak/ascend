package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.presentation.main.home.community_feed.diffutil.ChannelDiffUtilCallback

class ChannelAdapter(
    private val onChannelClick: (Channel) -> Unit
) : PagedListAdapter<Channel, ChannelViewHolder>(ChannelDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder.create(parent, onChannelClick)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}