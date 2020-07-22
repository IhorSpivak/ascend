package com.doneit.ascend.presentation.main.home.community_feed.post_details

import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewViewModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class PostDetailsViewModel(
    communityFeedUseCase: CommunityFeedUseCase,
    private val post: Post
) : CommentsViewViewModel(communityFeedUseCase, postId = post.id),
    PostDetailsContract.ViewModel