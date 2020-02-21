package com.doneit.ascend.presentation.video_chat.attachments.add_url

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.CreateAttachmentModel
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsContract

interface AddUrlContract {
    interface ViewModel : BaseViewModel {
        val model: CreateAttachmentModel
        val navigation: LiveData<AttachmentsContract.Navigation>
        val canSave : LiveData<Boolean>

        fun init(groupId: Long)
        fun backClick()
        fun onAddUrlClick()
    }

    interface Router {
        fun onBack()
    }

    enum class Navigation {
        BACK
    }
}