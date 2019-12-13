package com.doneit.ascend.presentation.main.home.group.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity

class GroupAdapter(
    private val items: MutableList<GroupEntity>,
    private var user: UserEntity? = null
) : RecyclerView.Adapter<GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder.create(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(items[position], user)
    }

    fun setUser(user: UserEntity) {
        this.user = user
    }

    fun updateData(newItems: List<GroupEntity>) {

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