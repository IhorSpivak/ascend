package com.doneit.ascend.presentation.main.home.community_feed.create_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreatePostModel
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

interface CreatePostContract {
    interface ViewModel : BaseViewModel {
        val createPostModel: PresentationCreatePostModel
        val canComplete: LiveData<Boolean>
        val attachments: LiveData<List<Attachment>>
        val showPopupEvent: SingleLiveEvent<String>
        val result: SingleLiveEvent<Post>
        val canAddAttachments: MutableLiveData<Boolean>

        fun backClick()
        fun createPost()
        fun setEditMode(post: Post)
        fun processSingleItem(uri: String, mimeType: String)
        fun deleteItemAt(pos: Int)
    }

    interface Router {
        fun onBack()
    }
}