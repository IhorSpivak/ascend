package com.doneit.ascend.presentation.video_chat.attachments

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.vrgsoft.networkmanager.livedata.SingleLiveManager

interface AttachmentsContract {
    interface ViewModel : BaseViewModel {
        val attachments: LiveData<PagedList<AttachmentEntity>>
        val user: LiveData<UserEntity?>
        val navigation: LiveData<Navigation>
        val showAddAttachmentDialog: SingleLiveManager<Unit>

        fun backClick()
        fun onDelete(id: Long)
        fun onAddAttachmentClick()
        fun onPhotoChosen(sourceUri: Uri)
        fun onFileChosen()
        fun onLinkAdded()
    }

    interface Router {
        fun onBack()
        fun toAddLinkFragment()
    }

    enum class Navigation {
        BACK
    }
}