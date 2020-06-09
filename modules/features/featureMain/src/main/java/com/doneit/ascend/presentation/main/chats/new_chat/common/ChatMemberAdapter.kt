package com.doneit.ascend.presentation.main.chats.new_chat.common

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.presentation.main.chats.common.ChatMembersDiffUtilCallback

class ChatMemberAdapter(
    private val onDeleteMember: (MemberEntity) -> Unit,
    private val onBlockMember: (MemberEntity) -> Unit
) : ListAdapter<MemberEntity, ChatMemberViewHolder>(ChatMembersDiffUtilCallback()), Filterable {

    var isUserMasterMind: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                notifyDataSetChanged()
            }
        }
    private var filteredList = listOf<MemberEntity>()
    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint?.toString().orEmpty()
            filteredList = if (charSearch.isEmpty()) {
                currentList
            } else {
                currentList.filter {
                    it.fullName.contains(charSearch, ignoreCase = true)
                }
            }
            return FilterResults().apply {
                values = filteredList
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredList = results?.values as List<MemberEntity>
            notifyDataSetChanged()
        }
    }

    override fun submitList(list: MutableList<MemberEntity>?) {
        super.submitList(list)
        filteredList = list.orEmpty()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMemberViewHolder {
        return ChatMemberViewHolder.create(parent, onDeleteMember, onBlockMember)
    }

    override fun onBindViewHolder(holder: ChatMemberViewHolder, position: Int) {
        holder.bind(filteredList[position],isUserMasterMind)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return filter
    }
}