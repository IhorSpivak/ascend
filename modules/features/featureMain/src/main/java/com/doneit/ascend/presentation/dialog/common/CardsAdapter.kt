package com.doneit.ascend.presentation.dialog.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.common.cards.CardViewHolder
import com.doneit.ascend.presentation.models.PresentationCardModel

class CardsAdapter(
    private val onAddCardClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var hasSelectionListener: ((Boolean) -> Unit)? = null
        set(value) {
            field = value
            checkSelection()
        }

    private val items = mutableListOf<PresentationCardModel>()
    private var lastSelectedIndex = -1

    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) {
            HOLDER_TYPE
        } else {
            ADD_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ADD_TYPE -> AddCardViewHolder.create(parent)
            else -> {
                CardViewHolder.create(parent)
            }
        }
    }

    override fun getItemCount() = items.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < items.size) {
            (holder as CardViewHolder).bind(items[position], {
                if (lastSelectedIndex != -1) {
                    items[lastSelectedIndex].isSelected = false
                    notifyItemChanged(lastSelectedIndex)
                }

                lastSelectedIndex = position
                items[position].isSelected = true

                checkSelection()
            })
        } else {
            holder.itemView.setOnClickListener {
                onAddCardClick.invoke()
            }
        }
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
        hasSelectionListener?.invoke(lastSelectedIndex != -1)
    }

    companion object {
        private const val HOLDER_TYPE = 0
        private const val ADD_TYPE = 1
    }
}