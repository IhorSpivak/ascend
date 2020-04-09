package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.models.ValidatableField

class ThemeAdapter(
    val viewModel: CreateWebinarContract.ViewModel
): RecyclerView.Adapter<ThemeViewHolder>() {

    var data = mutableListOf<ValidatableField>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        return ThemeViewHolder.create(parent).apply { setIsRecyclable(false) }
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(position, viewModel)
    }

    override fun getItemCount(): Int {
       return data.size
    }
}