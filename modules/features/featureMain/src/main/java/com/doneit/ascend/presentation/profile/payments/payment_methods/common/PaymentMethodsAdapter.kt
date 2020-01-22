package com.doneit.ascend.presentation.profile.payments.payment_methods.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.common.cards.CardDiffCallback
import com.doneit.ascend.presentation.common.cards.CardViewHolder
import com.doneit.ascend.presentation.models.PresentationCardModel

class PaymentMethodsAdapter(
    private val onDefaultChanged: (Long) -> Unit,
    private val onDeleteClickListener: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<PresentationCardModel>()
    private var lastSelectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CardViewHolder).bind(items[position], {
            if (lastSelectedIndex != -1) {
                items[lastSelectedIndex].isSelected = false
                notifyItemChanged(lastSelectedIndex)
            }

            if(items[position].isSelected.not()) {
                onDefaultChanged.invoke(items[position].id)
            }

            lastSelectedIndex = position
            items[position].isSelected = true//notify does not required a soon because the checkbox has hanged visual state yet

        }, onDeleteClickListener)
    }

    fun submitList(newItems: List<PresentationCardModel>) {
        val diff = DiffUtil.calculateDiff(
            CardDiffCallback(
                items,
                newItems
            )
        )

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }

    private fun resetSelection(position: Int, isSelected: Boolean) {
        items[position].isSelected = isSelected
        notifyItemChanged(position)
    }
}