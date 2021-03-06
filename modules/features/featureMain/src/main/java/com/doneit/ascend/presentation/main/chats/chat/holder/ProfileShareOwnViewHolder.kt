package com.doneit.ascend.presentation.main.chats.chat.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.common.setOnSingleClickListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOwnShareProfileBinding

class ProfileShareOwnViewHolder(
    itemView: View,
    private val onProfileClick: (UserEntity) -> Unit
) : BaseMessageHolder(itemView) {

    private val binding: ListItemOwnShareProfileBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(messageEntity: MessageEntity) {
        with(binding) {
            this.messageEntity = messageEntity
            myMessage.setOnSingleClickListener {
                messageEntity.sharedUser?.let { user -> onProfileClick(user) }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onProfileClick: (UserEntity) -> Unit
        ): ProfileShareOwnViewHolder {
            return ProfileShareOwnViewHolder(
                DataBindingUtil.inflate<ListItemOwnShareProfileBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_own_share_profile,
                    parent,
                    false
                ).root,
                onProfileClick
            )
        }
    }
}