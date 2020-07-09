package com.doneit.ascend.presentation.main.home.community_feed.create_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.getContentTypeFromMime
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreatePostModel
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

    private val media = arrayListOf<Attachment>()

    override val createPostModel: PresentationCreatePostModel = PresentationCreatePostModel()
    override val canComplete: LiveData<Boolean> = MutableLiveData(true)
    override val attachments = MutableLiveData<List<Attachment>>(media)
    override val showPopupEvent = SingleLiveEvent<String>()

    override fun backClick() {
        router.onBack()
    }

    override fun createPost() {
        communityFeedUseCase.createPost(
            viewModelScope,
            createPostModel.description.observableField.get().orEmpty(),
            media,
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
        if (media.size >= 5) return
        val index = media.indexOfFirst { it.url == uri }
        val newAttachment = Attachment(
            id = UUID.randomUUID().toString(),
            contentType = getContentTypeFromMime(mimeType),
            url = uri
        )
        if (index == -1) {
            media.add(newAttachment)
        } else media[index] = newAttachment
    }

    override fun deleteItemAt(pos: Int) {
        media.removeAt(pos)
        attachments.value = media
    }
}