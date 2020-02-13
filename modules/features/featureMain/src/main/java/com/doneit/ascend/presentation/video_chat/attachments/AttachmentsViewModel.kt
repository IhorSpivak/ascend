package com.doneit.ascend.presentation.video_chat.attachments

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class AttachmentsViewModel(
    private val attachmentsUseCase: AttachmentUseCase,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), AttachmentsContract.ViewModel {

    override val attachments = attachmentsUseCase.getAttachmentListPagedLive()
    override val user = userUseCase.getUserLive()
    override val showAddAttachmentDialog = SingleLiveManager(Unit)
    override val navigation = SingleLiveEvent<AttachmentsContract.Navigation>()

    override fun onAddAttachmentClick() {
        showAddAttachmentDialog.call()
    }

    override fun backClick() {
        navigation.postValue(AttachmentsContract.Navigation.BACK)
    }

    override fun onPhotoChosen(sourceUri: Uri) {
        // TODO:
    }

    override fun onFileChosen() {
        // TODO:
    }

    override fun onLinkAdded() {
        // TODO:
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