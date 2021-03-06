package com.doneit.ascend.presentation.main.chats.new_chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.models.PresentationCreateChatModel
import com.doneit.ascend.presentation.models.group.toDTO
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import kotlinx.coroutines.launch

class NewChatViewModel(
    private val router: NewChatContract.Router,
    private val localRouter: NewChatContract.LocalRouter,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(),
    NewChatContract.ViewModel {

    private lateinit var currentUser: UserEntity

    init {
        viewModelScope.launch {
            currentUser = userUseCase.getUser()!!
        }
    }

    private val isCompletable: MutableLiveData<Boolean> = MutableLiveData(false)
    private val members: MutableLiveData<List<AttendeeEntity>> = MutableLiveData()
    private val isAddMoreVisible: MutableLiveData<Boolean> = MutableLiveData(true)
    private val searchQuery = MutableLiveData<String>()


    override val newChatModel: PresentationCreateChatModel = PresentationCreateChatModel()
    override val addedMembers: LiveData<List<AttendeeEntity>> = members.switchMap {
        liveData {
            newChatModel.chatMembers = it.map { it.id }
            updateCanCreate()
            emit(it.toList())
        }
    }
    override val connectionAvailable: MutableLiveData<Boolean> = MutableLiveData(false)

    override val canAddMember: Boolean
        get() = selectedMembers.size < 50
    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()
    override val canComplete: LiveData<Boolean> = isCompletable.switchMap { liveData { emit(it) } }
    override val visibilityAddMore: LiveData<Boolean> =
        isAddMoreVisible.switchMap { liveData { emit(it) } }

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(it, currentUser.id)
        }

    override fun complete() {
        viewModelScope.launch {
            isCompletable.postValue(false)
            chatUseCase.createChat(newChatModel.toDTO()).let {
                if (it.isSuccessful) {
                    router.onBackWithOpenChat(it.successModel!!, currentUser, ChatType.CHAT)
                } else {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun onLocalBackPressed() {
        localRouter.onBack()
    }

    override fun onAddMember(member: AttendeeEntity) {
        selectedMembers.add(member)
        members.postValue(selectedMembers)
    }

    override fun onRemoveMember(member: AttendeeEntity) {
        selectedMembers.firstOrNull { it.id == member.id }?.let {
            selectedMembers.remove(it)
            members.postValue(selectedMembers)
        }
    }

    override fun onQueryTextChange(query: String) {
        searchQuery.postValue(query)
    }

    override fun onDeleteMember(id: Long) {
        selectedMembers.firstOrNull { it.id == id }?.let {
            selectedMembers.remove(it)
            members.postValue(selectedMembers)
            searchQuery.postValue(searchQuery.value)
        }
    }

    override fun addMember() {
        localRouter.navigateToAddChatMember()
    }

    private fun updateCanCreate() {
        var isFormValid = true

        isFormValid = isFormValid and newChatModel.chatMembers.isNotEmpty()
        isCompletable.postValue(isFormValid)
    }
}