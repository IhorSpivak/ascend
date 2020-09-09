package com.doneit.ascend.presentation.main.home.community_feed

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.CommunityFeedSocketEntity
import com.doneit.ascend.domain.entity.community_feed.CommunityFeedSocketEvent
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class CommunityFeedViewModel(
    private val postsUseCase: CommunityFeedUseCase,
    private val userUseCase: UserUseCase,
    private val chatUseCase: ChatUseCase,
    private val router: CommunityFeedContract.Router
) : BaseViewModelImpl(), CommunityFeedContract.ViewModel {

    override val community: MediatorLiveData<String?> = MediatorLiveData()
    override val posts =
        Transformations.switchMap(community) {
            postsUseCase.loadPosts(
                viewModelScope, CommunityFeedDTO(
                    perPage = 10,
                    sortType = SortType.DESC
                )
            )
        }

    override val channels = Transformations.switchMap(community) {
        chatUseCase.loadChats(
            viewModelScope,
            ChatListDTO(
                perPage = 10,
                sortColumn = "members_count",
                sortType = SortType.DESC,
                allChannels = true,
                chatType = ChatType.CHANNEL
            )
        )
    }

    private val socketMessage = postsUseCase.commentStream
    private lateinit var observer: Observer<CommunityFeedSocketEntity?>
    override val user = userUseCase.getUserLive()


    override fun initUser(user: UserEntity) {
        observer = getMessageObserver()
        socketMessage.observeForever(observer)
        user.community?.let {
            postsUseCase.connectToChannel(it.toLowerCase())
        }
    }

    init {
        community.addSource(user) {
            if (it?.community != null && community.value != it.community) {
                community.postValue(it.community)
            }
        }
    }

    override fun onNewPostClick() {
        router.navigateToCreatePost()
    }

    override fun onEditPostClick(post: Post) {
        router.navigateToCreatePost(post)
    }

    override fun onDeletePostClick(post: Post) {
        postsUseCase.deletePost(viewModelScope, postId = post.id, baseCallback = BaseCallback(
            onSuccess = {
                posts.value?.remove(post)
            },
            onError = {

            }
        ))
    }

    override fun onChannelClick(channel: ChatEntity) {
        if (channel.isSubscribed) {
            router.navigateToChannel(channel, user.value!!)
        } else {

        }
    }

    override fun onSeeAllClick() {
        router.navigateToChannels()
    }

    override fun leaveComment(postId: Long, message: String) {
        if (message.isBlank()) return
        postsUseCase.createPostComment(viewModelScope, postId, message, BaseCallback(
            onSuccess = {

            },
            onError = {

            }
        ))
    }

    override fun likePost(postId: Long) {
        postsUseCase.likePost(viewModelScope, postId, BaseCallback(
            onSuccess = {
                posts.value?.let {
                    val index = it.indexOfFirst { it.id == postId }
                    if (index != -1) {
                        val item = it[index]
                        it.set(
                            index,
                            item.copy(isLikedMe = true)
                        )
                    }
                }
            },
            onError = {

            }
        ))
    }

    override fun unlikePost(postId: Long) {
        postsUseCase.unlikePost(viewModelScope, postId, BaseCallback(
            onSuccess = {
                posts.value?.let {
                    val index = it.indexOfFirst { it.id == postId }
                    if (index != -1) {
                        val item = it[index]
                        it.set(
                            index,
                            item.copy(isLikedMe = false)
                        )
                    }
                }
            },
            onError = {

            }
        ))
    }

    override fun onUserClick(userId: Long) {
        router.navigateToMMInfo(userId)
    }

    override fun newItem(post: Post) {
        val index = posts.value?.indexOfFirst { it.id == post.id }
        if (index == null || index == -1) {
            posts.value?.add(0, post)
        } else {
            posts.value?.set(index, post)
        }
    }

    override fun reportUser(reason: String, userId: Long) {
        viewModelScope.launch {
            userUseCase.report(reason, userId.toString()).let {
                if (it.isSuccessful.not()) {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun blockUser(userId: Long) {
        viewModelScope.launch {
            chatUseCase.blockUser(userId).let {
                if (it.isSuccessful) {
                    //TODO:
                }
            }
        }
    }

    override fun attachmentClicked(attachments: List<Attachment>, selected: Int) {
        router.navigateToPreview(attachments, selected)
    }

    override fun updateCommentsCount(postId: Long, commentsCount: Int) {
        posts.value?.let {
            val index = it.indexOfFirst { it.id == postId }
            if (index != -1) {
                val item = it[index]
                it.set(index, item.copy(commentsCount = commentsCount))
            }
        }
    }

    override fun onJoinChannel(channel: ChatEntity) {
        viewModelScope.launch {
            if (chatUseCase.joinChannel(viewModelScope, channel.id).isSuccessful) {
                router.navigateToChannel(channel, user.value!!)
            }
        }
    }

    override fun onSharePostClick(id: Long) {
        router.navigateToShare(id, user.value!!, SharePostBottomSheetFragment.ShareType.POST)
    }

    override fun onCleared() {
        super.onCleared()
        postsUseCase.disconnect()
        socketMessage.removeObserver(observer)
    }

    private fun getMessageObserver(): Observer<CommunityFeedSocketEntity?> {
        return Observer { socketEvent ->
            socketEvent?.let {
                when (socketEvent.event) {
                    CommunityFeedSocketEvent.POST_COMMENTED -> {
                        val post = posts.value?.firstOrNull { it.id == socketEvent.postId }
                        val index = posts.value?.indexOf(post)
                        post?.let {
                            if (index != null && index != -1) {
                                posts.value?.set(
                                    index,
                                    it.copy(commentsCount = socketEvent.commentsCount)
                                )
                            }
                        }
                    }
                    CommunityFeedSocketEvent.POST_LIKED -> {
                        val post = posts.value?.firstOrNull { it.id == socketEvent.postId }
                        val index = posts.value?.indexOf(post)
                        post?.let {
                            if (index != null && index != -1) {
                                posts.value?.set(
                                    index,
                                    it.copy(likesCount = socketEvent.likesCount)
                                )
                            }
                        }
                    }
                    else -> {
                        throw IllegalArgumentException("unknown socket type")
                    }
                }
            }
        }
    }
}