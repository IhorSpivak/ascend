package com.doneit.ascend.presentation.video_chat.attachments

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.CreateAttachmentFileModel
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import java.io.File

interface AttachmentsContract {
    interface ViewModel : BaseViewModel {

        val model: CreateAttachmentFileModel
        val attachments: LiveData<PagedList<AttachmentEntity>>
        val user: LiveData<UserEntity?>
        val navigation: LiveData<Navigation>
        val showAddAttachmentDialog: SingleLiveManager<Unit>
        val transferEvents: LiveData<TransferEvent>
        val showPreview: LiveData<File>

        fun backClick()
        fun onDelete(id: Long)
        fun onAddAttachmentClick()
        fun init(groupId: Long)
        fun setAttachmentType(attachmentType: AttachmentType)
        fun uploadFile(path: String)
        fun downloadAttachment(attachment: AttachmentEntity, basePath: String)
    }

    interface Router {
        fun onBack()
        fun toAddLinkFragment(groupId: Long)
    }

    enum class Navigation {
        BACK
    }
}