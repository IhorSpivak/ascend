package com.doneit.ascend.presentation.video_chat.attachments.add_url

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.CreateAttachmentModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.Constants.DEFAULT_MODEL_ID
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsContract
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class AddUrlViewModel(
    private val attachmentUseCase: AttachmentUseCase
) : BaseViewModelImpl(), AddUrlContract.ViewModel {

    override val model = CreateAttachmentModel(DEFAULT_MODEL_ID, AttachmentType.LINK, isPrivate = false)
    override val navigation = SingleLiveEvent<AttachmentsContract.Navigation>()
    override val canSave = MutableLiveData<Boolean>()

    init {
        model.name.validator = { s ->
            val result = ValidationResult()

            if (s.isBlank()) {
                result.isSucceed = false
            }

            result
        }

        model.link.validator = { s ->
            val result = ValidationResult()

            if (s.isBlank()) {
                result.isSucceed = false
            }

            result
        }

        val invalidationListener = { updateCanSave() }
        model.name.onFieldInvalidate = invalidationListener
        model.link.onFieldInvalidate = invalidationListener
    }

    private fun updateCanSave() {
        var isFormValid = true

        isFormValid = isFormValid and model.name.isValid
        isFormValid = isFormValid and model.link.isValid

        canSave.postValue(isFormValid)
    }

    override fun init(groupId: Long) {
        model.groupId = groupId
    }

    override fun backClick() {
        navigation.postValue(AttachmentsContract.Navigation.BACK)
    }

    override fun onAddUrlClick() {
        viewModelScope.launch {
            val result = attachmentUseCase.createAttachment(model.toEntity())

            if (result.isSuccessful) {
                backClick()
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

}