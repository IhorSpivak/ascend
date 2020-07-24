package com.doneit.ascend.presentation.main.home.community_feed.channels

import androidx.lifecycle.*
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.chat.ChannelsWithUser
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class ChannelsViewModel(
    private val router: ChannelsContract.Router,
    userUseCase: UserUseCase,
    private val chatUseCase: ChatUseCase
) : BaseViewModelImpl(), ChannelsContract.ViewModel {
    override val channelWithCurrentUser: MediatorLiveData<ChannelsWithUser> = MediatorLiveData()
    override lateinit var channels: LiveData<PagedList<ChatEntity>>
    override val filterTextAll: MutableLiveData<String> = MutableLiveData("")
    override val user = userUseCase.getUserLive()


    init {
        channels = Transformations.switchMap(filterTextAll) { query ->
            val model = ChatListDTO(
                perPage = 10,
                sortColumn = "last_message",
                sortType = SortType.DESC,
                title = query,
                chatType = ChatType.CHANNEL
            )
            return@switchMap chatUseCase.loadChats(viewModelScope, model)
        }

        channelWithCurrentUser.addSource(user) {
            applyData(channels.value, it)
        }

        channelWithCurrentUser.addSource(channels) {
            applyData(it, user.value)
        }
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun onNewChannelPressed() {
        router.navigateToNewChannel()
    }

    override fun onNewChatPressed() {
        router.navigateToNewChannel()
    }

    override fun onChannelPressed(channel: ChatEntity) {
        router.navigateToChannel(channel.id)
    }

    override fun onJoinChannel(channel: ChatEntity) {
        router.navigateToChannel(channel.id)
    }

    override fun onLeaveChannel(channel: ChatEntity) {
        TODO("Not yet implemented")
    }

    private fun applyData(chatEntity: PagedList<ChatEntity>?, user: UserEntity?) {
        if (chatEntity != null && user != null) {
            channelWithCurrentUser.postValue(
                ChannelsWithUser(
                    chatEntity,
                    user
                )
            )
        }
    }

}