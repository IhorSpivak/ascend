package com.doneit.ascend.presentation.common.cards

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.presentation.models.PresentationCardModel

class CardDiffCallback(
    private val oldItems: List<PresentationCardModel>,
    private val newItems: List<PresentationCardModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size
}