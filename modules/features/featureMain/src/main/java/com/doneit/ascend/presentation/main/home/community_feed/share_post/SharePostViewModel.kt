package com.doneit.ascend.presentation.main.home.community_feed.share_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.dto.SharePostDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.community_feed.SharePostFilter

class SharePostViewModel(
    private val router: SharePostContract.Router,
    private val communityFeedUseCase: CommunityFeedUseCase,
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val postId: Long
) : BaseViewModelImpl(), SharePostContract.ViewModel {
    override lateinit var chats: LiveData<PagedList<ChatEntity>>
    override val channels: LiveData<PagedList<ChatEntity>>
    override val users: LiveData<PagedList<UserEntity>>
    override val filterTextAll: MutableLiveData<String> = MutableLiveData("")
    override val sharePostFilter: MutableLiveData<SharePostFilter> =
        MutableLiveData(SharePostFilter.CHAT)


    init {
        chats = Transformations.switchMap(filterTextAll) { query ->
            val model = ChatListDTO(
                perPage = 10,
                sortColumn = "last_message",
                sortType = SortType.DESC,
                title = query,
                chatType = ChatType.CHAT
            )
            return@switchMap chatUseCase.getMyChatListLive(model)
        }

        channels = Transformations.switchMap(filterTextAll) { query ->
            val model = ChatListDTO(
                perPage = 10,
                sortColumn = "last_message",
                sortType = SortType.DESC,
                title = query,
                chatType = ChatType.CHANNEL
            )
            return@switchMap chatUseCase.getMyChatListLive(model)
        }

        users = Transformations.switchMap(filterTextAll) { query ->
            return@switchMap null
        }
    }

    override fun onBackPressed() {

    }

    override fun shareChat(chatId: Long) {
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
}