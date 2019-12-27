package com.doneit.ascend.presentation.main.notification.common

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.dto.NotificationType
import com.doneit.ascend.domain.entity.dto.parseToNotificationType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateNotificationItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder
import com.doneit.ascend.presentation.utils.toNotificationDate

class NotificationViewHolder(
    private val binding: TemplateNotificationItemBinding
) : SearchViewHolder(binding.root) {

    private var posX: Float = 0F
    private var posY: Float = 0F

    fun bind(item: NotificationEntity, onDeleteListener: (id: Long) -> Unit, onClickListener: (id: Long) -> Unit) {
        binding.item = item
        itemView.isClickable = true

        itemView.setOnTouchListener { _, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                posX = motionEvent.rawX
                posY = motionEvent.rawY
            }

            if(motionEvent.action == MotionEvent.ACTION_MOVE) {
                posX = 0F
                posY = 0F
            }

            if(motionEvent.action == MotionEvent.ACTION_UP) {
                if (posX == motionEvent.rawX && posY == motionEvent.rawY) {
                    // click
                    onClickListener.invoke(item.groupId!!)
                }
            }

            true
        }

        try {
            binding.date = item.createdAt!!.toNotificationDate()

            when (item.notificationType.parseToNotificationType()) {
                NotificationType.INVITE_TO_A_MEETING -> {
                    binding.owner =
                        "${binding.root.context.getString(R.string.from)} ${item.owner?.fullName}"
                    binding.title = binding.root.context.getString(R.string.you_got_invite)
                }
                NotificationType.NEW_GROUPS -> {
                    binding.owner =
                        "${binding.root.context.getString(R.string.by)} ${item.owner?.fullName}"
                    binding.title = binding.root.context.getString(R.string.new_group)
                }
                NotificationType.MEETING_STARTED -> {
                    binding.owner =
                        "${binding.root.context.getString(R.string.by)} ${item.owner?.fullName}"
                    binding.title = binding.root.context.getString(R.string.group_started)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.date = ""
        }

        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id!!)
        }

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): NotificationViewHolder {
            val binding: TemplateNotificationItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_notification_item,
                parent,
                false
            )

            return NotificationViewHolder(binding)
        }
    }
}