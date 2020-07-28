package com.doneit.ascend.presentation.main.home.community_feed.share_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.dto.SharePostDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreateChatModel
import com.doneit.ascend.presentation.models.community_feed.SharePostFilter
import com.doneit.ascend.presentation.models.group.toDTO
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import kotlinx.coroutines.launch

class SharePostViewModel(
    private val router: SharePostContract.Router,
    private val communityFeedUseCase: CommunityFeedUseCase,
    private val chatUseCase: ChatUseCase,
    private val postId: Long,
    private val userId: Long
) : BaseViewModelImpl(), SharePostContract.ViewModel {
    override val chats = MutableLiveData<PagedList<ChatEntity>>()
    override val channels = MutableLiveData<PagedList<ChatEntity>>()
    override val users = MutableLiveData<PagedList<AttendeeEntity>>()
    override val filterTextAll: MutableLiveData<String> = MutableLiveData("")
    override val sharePostFilter: MutableLiveData<SharePostFilter> =
        MutableLiveData(SharePostFilter.CHAT)


    init {
        updateSearch(filterTextAll.value!!)
    }

    override fun getChats(filter: String) {
        val model = ChatListDTO(
            perPage = 10,
            sortType = SortType.DESC,
            title = filter,
            chatType = ChatType.CHAT
        )
        val response = chatUseCase.getChatList(model)
        chats.value = response
    }

    override fun getChannels(filter: String) {
        val model = ChatListDTO(
            perPage = 10,
            sortType = SortType.DESC,
            title = filter,
            chatType = ChatType.CHANNEL
        )
        val response = chatUseCase.getChatList(model)
        channels.value = response
    }

    override fun getUsers(filter: String) {
        val response = communityFeedUseCase.getUsersListPaged(viewModelScope, filter, userId)
        users.value = response
    }

    override fun updateSearch(filter: String) {
        when(sharePostFilter.value!!){
            SharePostFilter.CHAT -> getChats(filter)
            SharePostFilter.CHANNEL -> getChannels(filter)
            SharePostFilter.USER -> getUsers(filter)
        }
    }


    override fun shareChat(chatId: Long) {
        //TODO: 500 internal server error
        val model = SharePostDTO(chatIds = listOf(chatId))
        communityFeedUseCase.sharePost(
            viewModelScope, postId, model, baseCallback = BaseCallback(
                onSuccess = {
                    router.navigateToSharedPostChat(chatId)
                },
                onError = {}
            )
        )
    }

    override fun shareToUser(userId: Long) {
        //TODO: 500 internal server error
        val model = SharePostDTO(userIds = listOf(userId))
        communityFeedUseCase.sharePost(
            viewModelScope, postId, model, baseCallback = BaseCallback(
                onSuccess = {

                    viewModelScope.launch {
                        val chatModel = PresentationCreateChatModel().apply {
                            chatMembers = listOf(userId)
                        }
                        chatUseCase.createChat(chatModel.toDTO()).let {
                            if (it.isSuccessful) {
                                shareChat(chatId = it.successModel!!.id)
                            } else {
                                showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                            }
                        }
                    }

                },
                onError = {}
            )
        )
    }
}