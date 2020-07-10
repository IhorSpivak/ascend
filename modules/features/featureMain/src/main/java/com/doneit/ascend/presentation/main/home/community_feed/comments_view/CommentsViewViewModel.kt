package com.doneit.ascend.presentation.main.home.community_feed.comments_view

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.CommentsDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl

class CommentsViewViewModel(
    private val communityFeedUseCase: CommunityFeedUseCase,
    postId: Long
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
}