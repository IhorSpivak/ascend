package com.doneit.ascend.presentation.main.home.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class ChannelsViewModel(
    private val router: ChannelsContract.Router,
    chatUseCase: ChatUseCase
): BaseViewModelImpl(), ChannelsContract.ViewModel {

    override val communityList = MutableLiveData<List<Community>>()
    override lateinit var channelList: LiveData<PagedList<ChatEntity>>

    init {
        val model = ChatListDTO(
            perPage = COUNT_ON_PAGE_CHANNELS,
            sortColumn = SORT_COLUMN_CHANNELS,
            sortType = SortType.DESC,
            title = null,
            allChannels = true,
            chatType = ChatType.CHANNEL
        )
        channelList = chatUseCase.loadChats(viewModelScope, model)
    }

    override fun fetchCommunityList() {
        communityList.postValue(Community.values().toList())
    }

    companion object {

        private const val SORT_COLUMN_CHANNELS = "members_count"
        private const val COUNT_ON_PAGE_CHANNELS = 10

    }

}