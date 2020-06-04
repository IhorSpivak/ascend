package com.doneit.ascend.presentation.main.chats.chat_members

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ChatMembersContract {
    interface ViewModel : BaseViewModel {

        val members: LiveData<MutableList<MemberEntity>>
        val query: LiveData<String>

        fun setMembers(chatId: Long, members: MutableList<MemberEntity>)
        fun onQueryUpdated(newText: CharSequence)
        fun removeMember(member: MemberEntity)
        fun blockMember(member: MemberEntity)
        fun goBack()
    }

    interface Router {
        fun onBack()
    }
}