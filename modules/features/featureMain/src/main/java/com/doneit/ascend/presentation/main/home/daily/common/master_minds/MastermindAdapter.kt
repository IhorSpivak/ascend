package com.doneit.ascend.presentation.main.home.daily.common.master_minds

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.MasterMindEntity

class MastermindAdapter(
    private val items: MutableList<MasterMindEntity>,
    private val onItemClick:(MasterMindEntity)->Unit
) : RecyclerView.Adapter<MastermindViewHolder>() {
    init {
        items.sortWith(compareBy {it.fullName})
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MastermindViewHolder {
        return MastermindViewHolder.create(
            parent
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MastermindViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(items[position])
        }
    }

    fun updateData(newItems: List<MasterMindEntity>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }

            override fun getOldListSize() = items.size

            override fun getNewListSize() = newItems.size
        })

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }
}