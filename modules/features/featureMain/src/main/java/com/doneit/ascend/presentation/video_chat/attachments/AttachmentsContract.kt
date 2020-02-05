package com.doneit.ascend.presentation.video_chat.attachments

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AttachmentsContract {
    interface ViewModel : BaseViewModel {
        val attachments: LiveData<PagedList<AttachmentEntity>>

        fun backClick()
        fun onDelete(id: Long)
    }

    interface Router {
        fun onBack()
    }
}