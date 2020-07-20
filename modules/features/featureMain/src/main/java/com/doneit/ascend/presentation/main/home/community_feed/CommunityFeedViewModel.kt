package com.doneit.ascend.presentation.main.home.community_feed

import androidx.lifecycle.Observer
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
    override val posts = postsUseCase.loadPosts(
        viewModelScope, CommunityFeedDTO(
            perPage = 10,
            sortType = SortType.DESC
        )
    )
    override val channels = chatUseCase.loadChats(
        viewModelScope,
        ChatListDTO(
            perPage = 10,
            sortType = SortType.DESC,
            chatType = ChatType.CHANNEL
        )
    )


    private val socketMessage = postsUseCase.commentStream
    private lateinit var observer: Observer<CommunityFeedSocketEntity?>

    override fun initUser(user: UserEntity) {
        user.community?.let {
            postsUseCase.connectToChannel(it.toLowerCase())
            observer = getMessageObserver()
            socketMessage.observeForever(observer)
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

    }

    override fun onSeeAllClick() {
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
                            item.copy(likesCount = item.likesCount.inc(), isLikedMe = true)
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
                            item.copy(likesCount = item.likesCount.dec(), isLikedMe = false)
                        )
                    }
                }
            },
            onError = {

            }
        ))
    }

    override fun onUserClick(userId: Long) {
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
                    else -> {
                        throw IllegalArgumentException("unknown socket type")
                    }
                }
            }
        }
    }
}