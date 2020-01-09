package com.doneit.ascend.presentation.profile.payments.payment_methods.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.common.CardViewHolder
import com.doneit.ascend.presentation.models.PresentationCardModel

class PaymentMethodsAdapter(
    private val onDeleteClickListener: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var hasSelectionListener: ((Boolean) -> Unit)? = null
        set(value) {
            field = value
            checkSelection()
        }

    private val items = mutableListOf<PresentationCardModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CardViewHolder).bind(items[position], {
            val lastSelectedIndex = items.indexOfFirst { it.isSelected }
            if (lastSelectedIndex != -1) {
                items[lastSelectedIndex].isSelected = false
                notifyItemChanged(lastSelectedIndex)
            }

            items[position].isSelected = true
            notifyItemChanged(position)

            checkSelection()
        }, onDeleteClickListener)
    }

    fun setData(newItems: List<PresentationCardModel>) {
        items.clear()
        items.addAll(newItems)

        checkSelection()
        notifyDataSetChanged()
    }

    fun getSelectedItem(): PresentationCardModel? {
        return items.firstOrNull { it.isSelected }
    }

    private fun checkSelection() {
        val lastSelectedIndex = items.indexOfFirst { it.isSelected }
        hasSelectionListener?.invoke(lastSelectedIndex != -1)
    }
}