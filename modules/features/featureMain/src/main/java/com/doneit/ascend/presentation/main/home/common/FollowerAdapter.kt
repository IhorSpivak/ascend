package com.doneit.ascend.presentation.main.home.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.UserEntity

class FollowerAdapter(
    private val items: MutableList<UserEntity>
) : RecyclerView.Adapter<FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        return FollowerViewHolder.create(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.bind(items[position])
    }
}