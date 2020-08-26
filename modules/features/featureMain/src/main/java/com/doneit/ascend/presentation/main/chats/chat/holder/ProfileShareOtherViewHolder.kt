package com.doneit.ascend.presentation.main.chats.chat.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.common.setOnSingleClickListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOtherShareProfileBinding
import com.doneit.ascend.presentation.utils.extensions.getTimeFormat

class ProfileShareOtherViewHolder(
    itemView: View,
    private val onProfileClick: (UserEntity) -> Unit
) : BaseMessageHolder(itemView) {

    private val binding: ListItemOtherShareProfileBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(
        messageEntity: MessageEntity,
        memberEntity: MemberEntity
    ) {
        with(binding) {
            this.memberEntity = memberEntity
            this.messageEntity = messageEntity
            if(messageEntity.createdAt != null) {
                messageTime.text = root.context.getTimeFormat().format(messageEntity.createdAt)
            }
            itemView.setOnSingleClickListener {
                messageEntity.sharedUser?.let { user -> onProfileClick(user) }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onProfileClick: (UserEntity) -> Unit
        ): ProfileShareOtherViewHolder {
            return ProfileShareOtherViewHolder(
                DataBindingUtil.inflate<ListItemOtherShareProfileBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_other_share_profile,
                    parent,
                    false
                ).root,
                onProfileClick
            )
        }
    }
}