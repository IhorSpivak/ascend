package com.doneit.ascend.presentation.main.group_info.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ParticipantEntity

class InvitedParticipantAdapter: RecyclerView.Adapter<InviteViewHolder>() {

    var participants = emptyList<ParticipantEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder {
        return InviteViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    override fun onBindViewHolder(holder: InviteViewHolder, position: Int) {
        holder.bind(participants[position])
    }
}