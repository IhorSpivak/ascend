package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity

class MemberListAdapter: RecyclerView.Adapter<MemberViewHolder>() {

    var members = emptyList<String>()
    set(value) {
        field = value
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(AttendeeEntity(1,"name","name@mail.com", ""))
    }
}