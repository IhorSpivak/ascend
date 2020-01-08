package com.doneit.ascend.presentation.dialog.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateAddPaymentBinding

class AddCardViewHolder(
    private val binding: TemplateAddPaymentBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup): AddCardViewHolder {
            val binding: TemplateAddPaymentBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_add_payment,
                parent,
                false
            )

            return AddCardViewHolder(binding)
        }
    }
}
