package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.create_group.master_mind.common.AttendeeViewHolder

class MemberListAdapter: RecyclerView.Adapter<AttendeeViewHolder>() {

    var members = emptyList<String>()
    set(value) {
        field = value
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeViewHolder {
        return AttendeeViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: AttendeeViewHolder, position: Int) {
        holder.bind(AttendeeEntity(1,"name","name@mail.com", ""))
    }
}