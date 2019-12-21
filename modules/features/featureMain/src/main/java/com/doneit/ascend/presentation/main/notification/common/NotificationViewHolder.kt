package com.doneit.ascend.presentation.main.notification.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.dto.NotificationType
import com.doneit.ascend.domain.entity.dto.parseToNotificationType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateNotificationItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder
import com.doneit.ascend.presentation.utils.toDate
import java.text.SimpleDateFormat
import java.util.*

class NotificationViewHolder(
    private val binding: TemplateNotificationItemBinding
) : SearchViewHolder(binding.root) {

    fun bind(item: NotificationEntity, onDeleteListener: (id: Long) -> Unit) {
        binding.item = item

        try {
            val formatter = SimpleDateFormat("dd.MM.YY hh:mm aa", Locale.getDefault())

            binding.date = formatter.format(item.createdAt!!.toDate())

            when (item.notificationType.parseToNotificationType()) {
                NotificationType.INVITE_TO_A_MEETING -> {
                    binding.owner = "${binding.root.context.getString(R.string.from)} ${item.owner?.fullName}"
                    binding.title = binding.root.context.getString(R.string.you_got_invite)
                }
                NotificationType.NEW_GROUPS -> {
                    binding.owner = "${binding.root.context.getString(R.string.by)} ${item.owner?.fullName}"
                    binding.title = binding.root.context.getString(R.string.new_group)
                }
                NotificationType.MEETING_STARTED -> {
                    binding.owner = "${binding.root.context.getString(R.string.by)} ${item.owner?.fullName}"
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