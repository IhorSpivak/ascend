package com.doneit.ascend.presentation.main.notification.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.NotificationEntity

class NotificationsAdapter(
    private val onItemClick: (id: Long) -> Unit,
    private val onDeleteListener: (id: Long) -> Unit
) : PagedListAdapter<NotificationEntity, NotificationViewHolder>(NotificationDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position)!!, {
            onDeleteListener.invoke(it)
            notifyItemRemoved(position)
        }, onItemClick)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!
    }
}