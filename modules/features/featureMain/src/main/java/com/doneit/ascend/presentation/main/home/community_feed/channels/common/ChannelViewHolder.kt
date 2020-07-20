package com.doneit.ascend.presentation.main.home.community_feed.channels.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemChannelBinding
import java.text.DecimalFormat

class ChannelViewHolder(
    itemView: View,
    private val onChannelClick: (ChatEntity) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ListItemChannelBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(channel: ChatEntity) {
        with(binding) {
            model = channel
            itemView.setOnClickListener {
                onChannelClick(channel)
            }
            membersCount.text =  String.format(itemView.context.getString(R.string.channel_member_count),format(channel.membersCount))
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onChannelClick: (ChatEntity) -> Unit
        ): ChannelViewHolder {
            return ChannelViewHolder(
                DataBindingUtil.inflate<ListItemChannelBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_channel,
                    parent,
                    false
                ).root,
                onChannelClick
            )
        }
        private val suffix = arrayOf("", "k", "m", "b", "t")
        private const val MAX_LENGTH = 4
    }



    private fun format(number: Int): String? {
        var r: String = DecimalFormat("##0E0").format(number)
        r = r.replace(
            "E[0-9]".toRegex(),
            suffix[Character.getNumericValue(r[r.length - 1]) / 3]
        )
        while (r.length > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]".toRegex())) {
            r = r.substring(0, r.length - 2) + r.substring(r.length - 1)
        }
        return r
    }
}