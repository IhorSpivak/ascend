package com.doneit.ascend.presentation.common

import android.view.LayoutInflater
import android.view.View
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
    fun bind(item: PresentationCardModel, onSelection: () -> Unit, onDeleteClick: ((Long) -> Unit)? = null) {
        binding.item = item

        when(item.brand) {
            CardType.VISA -> binding.ivCardType.setImageResource(R.drawable.ic_visa)
            else -> binding.ivCardType.setImageResource(R.drawable.ic_master_card_local)
        }

        binding.chbSelection.setOnCheckedChangeListener { compoundButton, b ->
            onSelection.invoke()
        }

        if(onDeleteClick != null) {
            binding.tvDelete.visibility = View.VISIBLE
            binding.tvDelete.setOnClickListener {
                onDeleteClick.invoke(item.id)
            }
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
