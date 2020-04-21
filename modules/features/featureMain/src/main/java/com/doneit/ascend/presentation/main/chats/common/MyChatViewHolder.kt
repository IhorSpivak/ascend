package com.doneit.ascend.presentation.main.chats.common

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
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
        onClickListener: (chat: ChatEntity) -> Unit
    ) {
        binding.item = item
        itemView.isClickable = true
        binding.date = item.lastMessage?.updatedAt?.toChatDate(itemView.context)
        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id)
        }
        itemView.message.text = item.lastMessage?.message
        val res = when (item.lastMessage?.status) {
            MessageStatus.SENT -> R.drawable.ic_unread_message
            MessageStatus.READ -> R.drawable.ic_read_message
            MessageStatus.DELIVERED -> R.drawable.ic_sent_message
            else -> 0
        }
        itemView.messageStatus.setImageResource(res)

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
}