package com.doneit.ascend.presentation.main.notification.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.notification.NotificationEntity

class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationEntity>() {
    override fun areItemsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
        return oldItem == newItem
    }
}