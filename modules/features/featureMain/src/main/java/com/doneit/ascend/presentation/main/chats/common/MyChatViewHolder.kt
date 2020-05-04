package com.doneit.ascend.presentation.main.chats.common

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateMyChatItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder
import com.doneit.ascend.presentation.utils.extensions.toChatDate
import kotlinx.android.synthetic.main.template_my_chat_item.view.*
import kotlin.math.abs

class MyChatViewHolder(
    private val binding: TemplateMyChatItemBinding
) : SearchViewHolder(binding.root) {

    private var lastX: Float = 0F
    private var lastY: Float = 0F

    fun bind(
        item: ChatEntity,
        onDeleteListener: (id: Long) -> Unit,
        onClickListener: (chat: ChatEntity) -> Unit,
        user: UserEntity
    ) {
        binding.item = item
        itemView.isClickable = true
        binding.date = item.lastMessage?.let { it.updatedAt?.toChatDate(itemView.context) }
            ?: item.updatedAt?.toChatDate(itemView.context)
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id)
        }


        item.lastMessage?.let {
            when (it.type) {
                MessageType.INVITE -> {
                    itemView.message.text = binding.root.context.resources.getString(
                        R.string.invite_message,
                        getMemberNameById(item, item.chatOwnerId),
                        getMemberNameById(item, it.userId)
                    )
                }
                MessageType.LEAVE -> {
                    itemView.message.text = binding.root.context.resources.getString(
                        R.string.leave_message,
                        getMemberNameById(item, it.userId)
                    )
                }
                else -> itemView.message.text = it.message
            }
        } ?: run {
            itemView.message.setText(R.string.no_messages_yet)
        }

        val res = when (item.lastMessage?.status) {
            MessageStatus.READ -> R.drawable.ic_read_message
            MessageStatus.ALL -> 0
            else -> item.lastMessage?.let {
                if (it.userId == user.id) {
                    R.drawable.ic_sent_message
                } else {
                    R.drawable.ic_unread_message
                }
            } ?: 0
        }
        itemView.messageStatus.setImageResource(res)
        Glide.with(itemView.groupPlaceholder)
            .load(R.drawable.ic_group_placeholder)
            .circleCrop()
            .into(itemView.groupPlaceholder)
        itemView.setOnTouchListener { _, motionEvent ->
            var status = false

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                lastX = motionEvent.rawX
                lastY = motionEvent.rawY
            }

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (abs(lastX - motionEvent.rawX) < MOVEMENT_DELTA
                    && abs(lastY - motionEvent.rawY) < MOVEMENT_DELTA
                ) {
                    onClickListener.invoke(item)
                    status = true
                }
            }

            status
        }
    }

    companion object {

        private const val MOVEMENT_DELTA = 10

        fun create(parent: ViewGroup): MyChatViewHolder {
            val binding: TemplateMyChatItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_my_chat_item,
                parent,
                false
            )

            return MyChatViewHolder(binding)
        }
    }

    private fun getMemberNameById(item: ChatEntity, id: Long): String {
        item.members?.firstOrNull {
            it.id == id
        }?.let { member ->
            return member.fullName
        }
        return ""
    }
}