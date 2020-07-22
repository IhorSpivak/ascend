package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel

import androidx.lifecycle.*
import com.doneit.ascend.domain.entity.chats.ChatEntity
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
    private val isCompletable: MutableLiveData<Boolean> = MutableLiveData(false)
    override val canComplete: LiveData<Boolean> = isCompletable.switchMap { liveData { emit(it) } }

    private var channel: ChatEntity? = null
    override val connectionAvailable: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun setEditMode(channel: ChatEntity) {
        this.channel = channel
    }

    override fun addMembers() {
        localRouter.navigateToAddChannelMembers()
        complete()
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