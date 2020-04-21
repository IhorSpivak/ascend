package com.doneit.ascend.presentation.main.chats.new_chat.add_members.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberContract
import com.doneit.ascend.presentation.main.databinding.ListItemAddChatMemberBinding

class ChatMemberViewHolder (
    private val binding: ListItemAddChatMemberBinding,
    private val onAdd: (member: AttendeeEntity) -> Unit,
    private val onRemove: (member: AttendeeEntity) -> Unit,
    val model: AddMemberContract.ViewModel
) : RecyclerView.ViewHolder(binding.root) {
    private var isAttended = false

    fun bind(member: AttendeeEntity?){
        binding.apply {
            user = member
            modelView = model
            this.setSelection(false)
            model.selectedMembers.forEach {
                member?.let {member ->
                    if((it.id == member.id)){
                        this.setSelection(true)
                    }
                }
            }
        }
        binding.apply {
            root.setOnClickListener {
                if (it.isSelected){
                    onRemove.invoke(member!!)
                    this.swap()
                }else{
                    if (model.canAddMember) {
                        onAdd.invoke(member!!)
                        this.swap()
                    }
                }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onAdd: (member: AttendeeEntity) -> Unit,
            onRemove: (member: AttendeeEntity) -> Unit,
            model: AddMemberContract.ViewModel
        ): ChatMemberViewHolder {
            val binding: ListItemAddChatMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_add_chat_member,
                parent,
                false
            )
            return ChatMemberViewHolder(binding, onAdd, onRemove, model)
        }
    }
    private fun View.swapSelection(){
        this.isSelected = !isSelected
    }
    private fun View.swapEnable(){
        this.isEnabled = !isEnabled
    }
    private fun ListItemAddChatMemberBinding.swap(){
        root.swapSelection()
        followerName.swapEnable()
        selection.swapEnable()
    }

    private fun ListItemAddChatMemberBinding.setSelection(state: Boolean){
        root.isSelected = state
        followerName.isEnabled = state
        selection.isEnabled = state
    }
}