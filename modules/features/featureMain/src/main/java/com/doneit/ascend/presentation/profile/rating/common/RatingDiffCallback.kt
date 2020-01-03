package com.doneit.ascend.presentation.profile.rating.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.RateEntity

class RatingDiffCallback : DiffUtil.ItemCallback<RateEntity>() {
    override fun areItemsTheSame(oldItem: RateEntity, newItem: RateEntity): Boolean {
        return oldItem.userId == newItem.userId && oldItem.createdAt == newItem.createdAt
    }

    override fun areContentsTheSame(oldItem: RateEntity, newItem: RateEntity): Boolean {
        return oldItem == newItem
    }
}