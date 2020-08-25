package com.doneit.ascend.presentation.main.home.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ChannelsViewModel(
    private val router: ChannelsContract.Router,
    private val chatUseCase: ChatUseCase,
    userUseCase: UserUseCase
): BaseViewModelImpl(), ChannelsContract.ViewModel {

    override val communityList = MutableLiveData<List<Community>>()
    override var channelList = MediatorLiveData<PagedList<ChatEntity>>()
    override val user = userUseCase.getUserLive()

    private lateinit var channels: LiveData<PagedList<ChatEntity>>

    init {
        fetchChannelsList()
    }

    override fun fetchCommunityList() {
        communityList.postValue(Community.values().toList())
    }

    override fun fetchChannelsList() {
        val model = ChatListDTO(
            perPage = COUNT_ON_PAGE_CHANNELS,
            sortColumn = SORT_COLUMN_CHANNELS,
            sortType = SortType.DESC,
            title = null,
            allChannels = true,
            chatType = ChatType.CHANNEL
        )
        channels = chatUseCase.loadChats(viewModelScope, model)
        channelList.addSource(channels) {
            channelList.postValue(it)
        }
    }

    override fun onChannelPressed(channel: ChatEntity) {
        user.value?.let { user ->
            router.navigateToChannel(channel, user)
        }
    }

    override fun onJoinChannel(channel: ChatEntity) {
        viewModelScope.launch {
            if (chatUseCase.joinChannel(viewModelScope, channel.id).isSuccessful) {
                user.value?.let { user ->
                    router.navigateToChannel(channel, user)
                }
            }
        }
    }

    companion object {

        private const val SORT_COLUMN_CHANNELS = "members_count"
        private const val COUNT_ON_PAGE_CHANNELS = 10

    }

}