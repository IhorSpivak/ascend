package com.doneit.ascend.presentation.login.first_time_login.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.presentation.login.first_time_login.common.view_holder.AnswerViewHolder

class AnswerOptionsAdapter(
    private val items: List<AnswerOptionEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnswerViewHolder.create(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AnswerViewHolder).bind(items[position])
    }
}