package com.doneit.ascend.presentation.main.create_group.master_mind.webinar.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.create_group.master_mind.webinar.CreateWebinarContract
import com.doneit.ascend.presentation.models.ValidatableField
import com.doneit.ascend.presentation.utils.GroupAction

class ThemeAdapter(
    val viewModel: CreateWebinarContract.ViewModel,
    private val group: GroupEntity?,
    private val what: GroupAction?
): RecyclerView.Adapter<ThemeViewHolder>() {

    var data = mutableListOf<ValidatableField>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        return ThemeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(position, viewModel, data[position], group, what)
    }

    override fun getItemCount(): Int {
       return data.size
    }
}