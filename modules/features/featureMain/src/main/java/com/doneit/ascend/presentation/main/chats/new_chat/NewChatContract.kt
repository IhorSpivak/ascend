package com.doneit.ascend.presentation.main.chats.new_chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberContract
import com.doneit.ascend.presentation.models.PresentationCreateChatModel

interface NewChatContract {
    interface ViewModel : BaseViewModel,
        AddMemberContract.ViewModel {
        val newChatModel: PresentationCreateChatModel
        val canComplete: LiveData<Boolean>
        val visibilityAddMore: LiveData<Boolean>
        val connectionAvailable: MutableLiveData<Boolean>
        fun onDeleteMember(id: Long)
        fun addMember()
        fun complete()
    }

    interface Router {
        fun onBackWithOpenChat(chat: ChatEntity)
        fun onBack()
        fun navigateToAddChatMember()
    }
    interface LocalRouter {
        fun onBack()
        fun navigateToAddChatMember()
    }
}