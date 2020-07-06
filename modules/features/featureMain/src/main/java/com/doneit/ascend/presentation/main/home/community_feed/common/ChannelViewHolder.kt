package com.doneit.ascend.presentation.main.home.community_feed.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemCommunityFeedBinding

class ChannelViewHolder(
    itemView: View,
    private val onChannelClickListener: (Channel) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemCommunityFeedBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(channel: Channel) {
        with(binding) {
            channelModel = channel
            onChannelClick = View.OnClickListener { onChannelClickListener(channel) }
        }
    }

    companion object {
        fun create(parent: ViewGroup, onChannelClick: (Channel) -> Unit): ChannelViewHolder {
            return ChannelViewHolder(
                DataBindingUtil.inflate<ListItemCommunityFeedBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_community_feed,
                    parent,
                    false
                ).root,
                onChannelClick
            )
        }
    }
}