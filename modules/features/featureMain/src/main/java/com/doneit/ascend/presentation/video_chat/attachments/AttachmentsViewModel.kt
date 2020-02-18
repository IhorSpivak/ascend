package com.doneit.ascend.presentation.video_chat.attachments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.entity.dto.CreateAttachmentDTO
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.CreateAttachmentFileModel
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.video_chat.attachments.listeners.AmazoneTransferListener
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.launch
import java.io.File


@CreateFactory
class AttachmentsViewModel(
    private val attachmentsUseCase: AttachmentUseCase,
    private val userUseCase: UserUseCase,
    private val transferUtility: TransferUtility,
    private val s3Client: AmazonS3Client
) : BaseViewModelImpl(), AttachmentsContract.ViewModel {

    override val model = CreateAttachmentFileModel(
        Constants.DEFAULT_MODEL_ID,
        AttachmentType.UNEXPECTED,
        isPrivate = false
    )

    private val groupId = MutableLiveData<Long>()
    override val attachments = groupId.switchMap {
        attachmentsUseCase.getAttachmentListPagedLive(
            AttachmentsListDTO(
                groupId = it
            )
        )
    }
    override val user = userUseCase.getUserLive()
    override val showAddAttachmentDialog = SingleLiveManager(Unit)
    override val navigation = SingleLiveEvent<AttachmentsContract.Navigation>()
    override val transferEvents = SingleLiveEvent<TransferEvent>()

    private val modelsToUpload = hashMapOf<Int, CreateAttachmentDTO>()
    private val observers: MutableList<TransferObserver> by lazy {
        transferUtility.getTransfersWithType(TransferType.UPLOAD)
    }
    private val uploadListener = object : AmazoneTransferListener() {
        override fun onStateChanged(id: Int, state: TransferState?) {
            when (state) {
                TransferState.FAILED -> {
                    transferEvents.postValue(TransferEvent.ERROR)
                }
                TransferState.COMPLETED -> {
                    val url = s3Client.getUrl(Constants.AWS_BUCKET, model.name).toString()
                    val model = modelsToUpload[id] ?: model.toEntity()

                    createAttachment(
                        model.copy(
                            link = url
                        )
                    )
                    transferEvents.postValue(TransferEvent.COMPLETED)
                }
            }
        }
    }
    private val downloadListener = object : AmazoneTransferListener() {
        override fun onStateChanged(id: Int, state: TransferState?) {
            when (state) {
                TransferState.FAILED -> {
                    transferEvents.postValue(TransferEvent.ERROR)
                }

                TransferState.COMPLETED -> {
                    transferEvents.postValue(TransferEvent.COMPLETED)
                }
            }
        }
    }

    override fun onAddAttachmentClick() {
        showAddAttachmentDialog.call()
    }

    override fun backClick() {
        navigation.postValue(AttachmentsContract.Navigation.BACK)
    }

    override fun init(groupId: Long) {
        this.groupId.postValue(groupId)
        model.groupId = groupId
    }

    override fun setAttachmentType(attachmentType: AttachmentType) {
        model.attachmentType = attachmentType
    }

    override fun uploadFile(path: String) {
        val file = File(path)
        val observer = transferUtility.upload(
            Constants.AWS_BUCKET, file.name, file
        )

        model.size = observer.bytesTotal
        model.name = file.name

        observers.add(observer)
        modelsToUpload[observer.id] = model.toEntity()

        observer.setTransferListener(uploadListener)
        transferEvents.postValue(TransferEvent.STARTED)
    }

    override fun downloadAttachment(attachment: AttachmentEntity, basePath: String) {
        val path = basePath + File.separatorChar + attachment.fileName
        val file = File(path)
        if (file.exists().not()) {
            file.createNewFile()
        }

        val observer = transferUtility.download(
            Constants.AWS_BUCKET, attachment.fileName, file
        )
        observers.add(observer)
        observer.setTransferListener(downloadListener)
        transferEvents.postValue(TransferEvent.STARTED)
    }

    private fun createAttachment(model: CreateAttachmentDTO) {
        viewModelScope.launch {
            val result = attachmentsUseCase.createAttachment(model)

            if (result.isSuccessful.not()) {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
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