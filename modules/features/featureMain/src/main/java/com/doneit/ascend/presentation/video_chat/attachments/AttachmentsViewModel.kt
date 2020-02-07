package com.doneit.ascend.presentation.video_chat.attachments

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class AttachmentsViewModel(
    private val attachmentsUseCase: AttachmentUseCase,
    private val userUseCase: UserUseCase,
    private val router: AttachmentsContract.Router
) : BaseViewModelImpl(), AttachmentsContract.ViewModel {

    override val attachments = attachmentsUseCase.getAttachmentListPagedLive()
    override val user = userUseCase.getUserLive()

    override fun backClick() {
        router.onBack()
    }

    override fun onDelete(id: Long) {
        viewModelScope.launch {
            val response = attachmentsUseCase.delete(id)

            if (response.isSuccessful.not()) {
                showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
            }
        }
    }
}