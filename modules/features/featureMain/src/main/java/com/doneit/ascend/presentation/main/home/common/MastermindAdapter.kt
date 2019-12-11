package com.doneit.ascend.presentation.main.home.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.MasterMindEntity

class MastermindAdapter(
    private val items: MutableList<MasterMindEntity>
) : RecyclerView.Adapter<MastermindViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MastermindViewHolder {
        return MastermindViewHolder.create(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MastermindViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(newItems: List<MasterMindEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}