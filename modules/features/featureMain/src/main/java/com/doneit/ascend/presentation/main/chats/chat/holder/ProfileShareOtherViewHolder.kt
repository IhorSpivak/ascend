package com.doneit.ascend.presentation.main.chats.chat.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherShareProfileBinding

class ProfileShareOtherViewHolder(
    itemView: View
) : BaseMessageHolder(itemView) {
    override fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {

    }

    companion object {
        fun create(
            parent: ViewGroup
        ): ProfileShareOtherViewHolder {
            return ProfileShareOtherViewHolder(
                DataBindingUtil.inflate<ListItemOtherShareProfileBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_other_share_profile,
                    parent,
                    false
                ).root
            )
        }
    }
}