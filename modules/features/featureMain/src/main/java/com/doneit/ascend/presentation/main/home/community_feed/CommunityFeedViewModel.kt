package com.doneit.ascend.presentation.main.home.community_feed

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class CommunityFeedViewModel(
    private val postsUseCase: CommunityFeedUseCase
) : BaseViewModelImpl(), CommunityFeedContract.ViewModel {
    override val posts = postsUseCase.loadPosts(
        viewModelScope, CommunityFeedDTO(
            perPage = 10,
            sortType = SortType.DESC
        )
    )
    override val channels = postsUseCase.loadChannels(
        viewModelScope,
        CommunityFeedDTO(
            perPage = 10,
            sortType = SortType.DESC
        )
    )

    override fun onNewPostClick() {
    }

    override fun onChannelClick(channel: Channel) {

    }

    override fun onSeeAllClick() {
    }

    override fun leaveComment(postId: Long, message: String) {

    }

    override fun likePost(postId: Long) {
        postsUseCase.likePost(viewModelScope, postId, BaseCallback(
            onSuccess = {
            },
            onError = {

            }
        ))
    }

    override fun unlikePost(postId: Long) {
        postsUseCase.unlikePost(viewModelScope, postId, BaseCallback(
            onSuccess = {
            },
            onError = {

            }
        ))
    }

    override fun onUserClick(userId: Long) {
    }
}