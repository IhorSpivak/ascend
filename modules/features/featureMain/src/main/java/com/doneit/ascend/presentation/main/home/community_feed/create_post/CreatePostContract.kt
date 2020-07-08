package com.doneit.ascend.presentation.main.home.community_feed.create_post

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreatePostModel

interface CreatePostContract {
    interface ViewModel : BaseViewModel {
        val createPostModel: PresentationCreatePostModel
        val canComplete: LiveData<Boolean>

        fun backClick()
    }

    interface Router {
        fun onBack()
    }
}