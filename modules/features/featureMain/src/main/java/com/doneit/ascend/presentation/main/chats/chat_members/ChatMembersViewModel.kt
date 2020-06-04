package com.doneit.ascend.presentation.main.chats.chat_members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@ViewModelDiModule
@CreateFactory
class ChatMembersViewModel(
    private val chatUseCase: ChatUseCase,
    private val router: ChatMembersContract.Router
) : BaseViewModelImpl(), ChatMembersContract.ViewModel {

    override val members = MutableLiveData<MutableList<MemberEntity>>()
    override val query = MutableLiveData<String>()
    private var chatId: Long by Delegates.notNull()

    override fun setMembers(chatId: Long, members: MutableList<MemberEntity>) {
        this.members.value = members
        this.chatId = chatId
    }

    override fun onQueryUpdated(newText: CharSequence) {
        query.value = newText.toString()
    }

    override fun removeMember(member: MemberEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (chatUseCase.removeUser(chatId, member.id).isSuccessful) {
                members.value?.remove(member)
                members.postValue(members.value)
            }
        }
    }

    override fun blockMember(member: MemberEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (chatUseCase.blockUser(member.id).isSuccessful) {
                members.value?.remove(member)
                members.postValue(members.value)
            }
        }
    }

    override fun goBack() {
        router.onBack()
    }

}