package com.doneit.ascend.presentation.main.home.community_feed.comments_view

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CommentsViewContract {
    interface ViewModel : BaseViewModel {
        val comments: LiveData<PagedList<Comment>>
        val commentsCount: LiveData<Int>
        fun onDeleteComment(comment: Comment)
        fun leaveComment(message: String)
        fun setCommentsCount(count: Int)
    }
}