package com.doneit.ascend.presentation.profile.payments.my_transactions.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.PurchaseEntity

class TransactionsAdapter(
) : PagedListAdapter<PurchaseEntity, TransactionViewHolder>(TransactionsDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id
    }
}