package com.doneit.ascend.presentation.video_chat.attachments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class AttachmentsViewModel(
    private val attachmentsUseCase: AttachmentUseCase,
    private val router: AttachmentsContract.Router
) : BaseViewModelImpl(), AttachmentsContract.ViewModel {

    override val attachments = MutableLiveData<PagedList<AttachmentEntity>>()

    override fun loadData() {
        showProgress(true)
        viewModelScope.launch {
            val response = attachmentsUseCase.getAttachmentListPaged(AttachmentsListModel())
            attachments.postValue(response)
            showProgress(false)
        }
    }

    override fun backClick() {
        router.onBack()
    }
}