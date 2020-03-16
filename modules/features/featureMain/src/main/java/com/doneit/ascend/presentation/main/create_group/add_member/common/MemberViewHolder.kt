package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemMemberBinding

class MemberViewHolder(
    private val binding: ListItemMemberBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(name: String){
        binding.name = name
        binding.root.setOnClickListener {
            it.swapSelection()
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): MemberViewHolder {
            val binding: ListItemMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_member,
                parent,
                false
            )
            return MemberViewHolder(binding)
        }

        private const val FIRST = 1
        private const val SECOND = 2
    }
    fun View.swapSelection(){
        this.isSelected = !isSelected
    }
}