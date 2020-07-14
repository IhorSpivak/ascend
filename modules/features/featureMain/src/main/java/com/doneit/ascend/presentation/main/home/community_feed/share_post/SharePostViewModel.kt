package com.doneit.ascend.presentation.main.home.community_feed.share_post

import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl

class SharePostViewModel(
    private val communityFeedUseCase: CommunityFeedUseCase,
    private val postId: Long
) : BaseViewModelImpl(), SharePostContract.ViewModel{
    /*override fun getChats(filter: String): LiveData<PagedList<ChatEntity>> {
    }

    override fun getChannels(filter: String): LiveData<PagedList<ChatEntity>> {
    }

    override fun getUsers(filter: String): LiveData<PagedList<UserEntity>> {
    }*/

    override fun onBackPressed() {

    }

}