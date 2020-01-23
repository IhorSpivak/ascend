package com.doneit.ascend.presentation.profile.payments.my_transactions.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.PurchaseEntity

class TransactionsDiffCallback : DiffUtil.ItemCallback<PurchaseEntity>() {
    override fun areItemsTheSame(oldItem: PurchaseEntity, newItem: PurchaseEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PurchaseEntity, newItem: PurchaseEntity): Boolean {
        return oldItem == newItem
    }
}