package com.doneit.ascend.presentation.dialog.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.CardType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateCreditCardBinding
import com.doneit.ascend.presentation.models.PresentationCardModel

class CardViewHolder(
    private val binding: TemplateCreditCardBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PresentationCardModel, onSelection: () -> Unit) {
        binding.item = item

        when(item.brand) {
            CardType.VISA -> binding.ivCardType.setImageResource(R.drawable.ic_visa)
            else -> binding.ivCardType.setImageResource(R.drawable.ic_master_card)
        }

        binding.chbSelection.setOnCheckedChangeListener { compoundButton, b ->
            onSelection.invoke()
        }
    }

    companion object {
        fun create(parent: ViewGroup): CardViewHolder {
            val binding: TemplateCreditCardBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_credit_card,
                parent,
                false
            )

            return CardViewHolder(binding)
        }
    }
}
