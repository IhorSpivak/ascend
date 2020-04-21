package com.doneit.ascend.presentation.main.chats.new_chat

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberContract
import com.doneit.ascend.presentation.models.PresentationCreateChatModel

interface NewChatContract {
    interface ViewModel : BaseViewModel,
        AddMemberContract.ViewModel {
        val newChatModel: PresentationCreateChatModel
        val canComplete: LiveData<Boolean>
        val visibilityAddMore: LiveData<Boolean>
        fun onDeleteMember(id: Long)
        fun addMember()
        fun complete()
    }

    interface Router {
        fun onBack()
        fun navigateToAddChatMember()
    }
    interface LocalRouter {
        fun onBack()
        fun navigateToAddChatMember()
    }
}