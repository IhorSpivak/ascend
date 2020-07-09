package com.doneit.ascend.presentation.main.home.community_feed.create_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.getContentTypeFromMime
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreatePostModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import java.util.*

@CreateFactory
@ViewModelDiModule
class CreatePostViewModel(
    private val communityFeedUseCase: CommunityFeedUseCase,
    private val router: CreatePostContract.Router
) : BaseViewModelImpl(), CreatePostContract.ViewModel {

    override val createPostModel: PresentationCreatePostModel = PresentationCreatePostModel()
    override val canComplete = MutableLiveData(false)
    override val attachments = MutableLiveData<List<Attachment>>(createPostModel.media)
    override val showPopupEvent = SingleLiveEvent<String>()

    init {
        createPostModel.description.validator = {
            canComplete.value = it.length > 2
            ValidationResult(it.length > 2)
        }
    }

    override fun backClick() {
        router.onBack()
    }

    override fun createPost() {
        communityFeedUseCase.createPost(
            viewModelScope,
            createPostModel.description.observableField.get().orEmpty(),
            createPostModel.media,
            BaseCallback(
                onSuccess = {
                    router.onBack()
                },
                onError = {
                    showPopupEvent.postValue(it)
                }
            )
        )
    }

    override fun processSingleItem(uri: String, mimeType: String) {
        if (createPostModel.media.size >= 5) return
        val index = createPostModel.media.indexOfFirst { it.url == uri }
        val newAttachment = Attachment(
            id = UUID.randomUUID().toString(),
            contentType = getContentTypeFromMime(mimeType),
            url = uri
        )
        if (index == -1) {
            createPostModel.media.add(newAttachment)
        } else createPostModel.media[index] = newAttachment
    }

    override fun deleteItemAt(pos: Int) {
        createPostModel.media.removeAt(pos)
        attachments.value = createPostModel.media
    }
}