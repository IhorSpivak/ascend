package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreateChannelModel
import com.doneit.ascend.presentation.models.toEntity
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class CreateChannelViewModel(
    private val router: CreateChannelContract.Router,
    private val localRouter: CreateChannelContract.LocalRouter,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), CreateChannelContract.ViewModel {
    override val newChannelModel: PresentationCreateChannelModel = PresentationCreateChannelModel()
    private val isCompletable: MutableLiveData<Boolean> = MutableLiveData(true)
    override val canComplete: LiveData<Boolean> = isCompletable.switchMap { liveData { emit(it) } }

    private var channel: ChatEntity? = null
    override val connectionAvailable: MutableLiveData<Boolean> = MutableLiveData(true)
    override fun onPhotoSelected(
        sourceUri: Uri,
        destinationUri: Uri,
        fragmentToReceiveResult: Fragment
    ) {
        router.navigateToAvatarUCropActivity(sourceUri, destinationUri, fragmentToReceiveResult)
    }

    private val members: MutableLiveData<List<AttendeeEntity>> = MutableLiveData()
    private val searchQuery = MutableLiveData<String>()
    override val addedMembers: LiveData<List<AttendeeEntity>> = members.switchMap {
        liveData {
            newChannelModel.participants.set(it.map { it.id })
            emit(it.toList())
        }
    }
    private lateinit var currentUser: UserEntity

    init {
        viewModelScope.launch {
            currentUser = userUseCase.getUser()!!
        }
    }

    override fun setEditMode(channel: ChatEntity) {
        this.channel = channel
    }

    override fun addMembers() {
        localRouter.navigateToAddChannelMembers()
    }

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(it, currentUser.id)
        }
    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()
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

    override fun onDeleteMember(id: Long) {
        selectedMembers.firstOrNull { it.id == id }?.let {
            selectedMembers.remove(it)
            members.postValue(selectedMembers)
            searchQuery.postValue(searchQuery.value)
        }
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun onLocalBackPressed() {
        localRouter.onBack()
    }

    override fun onQueryTextChange(query: String) {
        searchQuery.postValue(query)
    }

    override fun complete() {
        viewModelScope.launch {
            channel?.let {
                chatUseCase.updateChannel(
                    viewModelScope,
                    it.id,
                    newChannelModel.toEntity()
                )
            } ?: run {
                chatUseCase.createChannel(viewModelScope, newChannelModel.toEntity())
            }
        }
    }
}