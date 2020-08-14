package com.doneit.ascend.presentation.main.notification.common

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.entity.notification.NotificationType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateNotificationItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder
import com.doneit.ascend.presentation.utils.extensions.toNotificationDate
import kotlin.math.abs

class NotificationViewHolder(
    private val binding: TemplateNotificationItemBinding
) : SearchViewHolder(binding.root) {

    private var lastX: Float = 0F
    private var lastY: Float = 0F

    fun bind(
        item: NotificationEntity,
        onDeleteListener: (id: Long) -> Unit,
        onClickListener: (id: Long) -> Unit
    ) {
        binding.item = item
        itemView.isClickable = true

        binding.date = item.createdAt?.toNotificationDate()

        val ownerFormat = when (item.notificationType) {
            NotificationType.INVITE_TO_A_MEETING -> {
                R.string.from

            }
            else -> R.string.by
        }

        binding.owner = "${binding.root.context.getString(ownerFormat)} ${item.owner?.fullName}"

        binding.ibDelete.setOnClickListener {
            onDeleteListener.invoke(item.id!!)
        }

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
                    onClickListener.invoke(item.groupId!!)
                    status = true
                }
            }

            status
        }
    }

    companion object {
        private const val MOVEMENT_DELTA = 10//empirically selection

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