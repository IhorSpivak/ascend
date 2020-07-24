package com.doneit.ascend.presentation.main.home.community_feed.post_details

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewViewModel
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class PostDetailsViewModel(
    communityFeedUseCase: CommunityFeedUseCase,
    private val postsUseCase: CommunityFeedUseCase,
    private val userUseCase: UserUseCase,
    private val router: PostDetailsContract.Router,
    post: Post
) : CommentsViewViewModel(communityFeedUseCase, postId = post.id),
    PostDetailsContract.ViewModel {

    override val currentPost = MediatorLiveData<Post>()

    private val post
        get() = requireNotNull(currentPost.value)

    init {
        currentPost.value = post
        currentPost.addSource(commentsCount) {
            currentPost.value = post.copy(commentsCount = it)
        }
    }

    override fun showUserDetails() {

    }

    override fun onEditPostClick() {
        router.navigateToCreatePost(post)
    }

    override fun onDeletePostClick() {
        postsUseCase.deletePost(viewModelScope,
            postId = post.id,
            baseCallback = BaseCallback(
                onSuccess = {

                },
                onError = {

                }
            ))
    }

    override fun likePost() {
        postsUseCase.likePost(viewModelScope, post.id, BaseCallback(
            onSuccess = {
                currentPost.postValue(
                    post.copy(
                        isLikedMe = true,
                        likesCount = ++post.likesCount
                    )
                )
            },
            onError = {

            }
        ))
    }

    override fun unlikePost() {
        postsUseCase.unlikePost(viewModelScope, post.id, BaseCallback(
            onSuccess = {
                currentPost.postValue(
                    post.copy(
                        isLikedMe = false,
                        likesCount = --post.likesCount
                    )
                )
            },
            onError = {

            }
        ))
    }

    override fun reportUser(reason: String) {
        viewModelScope.launch {
            userUseCase.report(reason, post.owner.id.toString()).let {
                if (it.isSuccessful.not()) {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun attachmentClicked(attachments: List<Attachment>, selected: Int) {
        router.navigateToPreview(attachments, selected)
    }
}