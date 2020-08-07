package com.doneit.ascend.presentation.main.create_group.master_mind.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity

class InvitedMembersAdapter(
    private val onDeleteMember: (AttendeeEntity) -> Unit
) : RecyclerView.Adapter<AttendeeViewHolder>() {

    private var list = mutableListOf<AttendeeEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeViewHolder {
        return AttendeeViewHolder.create(parent, onDeleteMember)
    }

    override fun onBindViewHolder(holder: AttendeeViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun submitList(list: MutableList<AttendeeEntity>) {
        DiffUtil
            .calculateDiff(AttendeeDiffCallback(this.list, list))
            .dispatchUpdatesTo(this)
        this.list = list
    }

    fun remove(index: Int){
        list.removeAt(index)
        notifyItemRemoved(index)

    }

    private class AttendeeDiffCallback(
        val oldList: List<AttendeeEntity>,
        val newList: List<AttendeeEntity>
    ) : DiffUtil.Callback() {
        override fun getNewListSize() = newList.size
        override fun getOldListSize() = oldList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

    }
}