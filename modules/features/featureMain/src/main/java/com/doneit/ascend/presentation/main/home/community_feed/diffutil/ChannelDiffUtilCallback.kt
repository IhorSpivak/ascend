package com.doneit.ascend.presentation.main.home.community_feed.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.community_feed.Channel

class ChannelDiffUtilCallback : DiffUtil.ItemCallback<Channel>() {
    override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
        return oldItem == newItem
    }

}