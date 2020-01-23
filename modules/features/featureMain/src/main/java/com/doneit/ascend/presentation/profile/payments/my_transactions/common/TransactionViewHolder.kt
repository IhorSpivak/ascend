package com.doneit.ascend.presentation.profile.payments.my_transactions.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.PurchaseEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplatePurchaseBinding

class TransactionViewHolder(
    private val binding: TemplatePurchaseBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PurchaseEntity) {
        binding.item = item
    }

    companion object {
        fun create(parent: ViewGroup): TransactionViewHolder {
            val binding: TemplatePurchaseBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_purchase,
                parent,
                false
            )

            return TransactionViewHolder(binding)
        }
    }
}
