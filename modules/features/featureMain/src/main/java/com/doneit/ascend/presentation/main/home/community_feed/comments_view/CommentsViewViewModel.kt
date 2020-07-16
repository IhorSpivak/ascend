package com.doneit.ascend.presentation.main.home.community_feed.comments_view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.dto.CommentsDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl

class CommentsViewViewModel(
    private val communityFeedUseCase: CommunityFeedUseCase,
    private val postId: Long
) : BaseViewModelImpl(), CommentsViewContract.ViewModel {
    override val comments = communityFeedUseCase.loadComments(
        coroutineScope = viewModelScope,
        postId = postId,
        commentsRequest = CommentsDTO(
            page = 0,
            perPage = 10,
            sortType = SortType.DESC,
            sortColumn = "created_at"
        )
    )
    override val commentsCount = MutableLiveData(0)

    override fun onDeleteComment(comment: Comment) {
        communityFeedUseCase.deleteComment(viewModelScope,
            postId = postId,
            commentId = comment.id,
            baseCallback = BaseCallback(
                onSuccess = {
                    comments.value?.remove(comment)
                    setCommentsCount(commentsCount.value!!.dec())
                },
                onError = {

                }
            ))
    }

    override fun leaveComment(message: String) {
        if (message.isBlank()) return
        communityFeedUseCase.createPostComment(viewModelScope, postId, message, BaseCallback(
            onSuccess = {
                newItem(it)
            },
            onError = {

            }
        ))
    }

    override fun setCommentsCount(count: Int) {
        commentsCount.postValue(count)
    }


    fun newItem(comment: Comment) {
        val index = comments.value?.indexOfFirst { it.id == comment.id }
        if (index == null || index == -1) {
            comments.value?.add(0, comment)
        } else {
            comments.value?.set(index, comment)
        }
    }
}