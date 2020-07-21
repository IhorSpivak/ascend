package com.doneit.ascend.presentation.main.home.community_feed.create_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.community_feed.Size
import com.doneit.ascend.domain.entity.community_feed.getContentTypeFromMime
import com.doneit.ascend.domain.use_case.interactor.community_feed.CommunityFeedUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreatePostModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

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
    override val result = SingleLiveEvent<Post>()
    override val canAddAttachments = MutableLiveData(true)
    private var post: Post? = null

    init {
        createPostModel.description.validator = {
            canComplete.value = it.length > 2 || createPostModel.media.size > 0
            ValidationResult(it.length > 2)
        }
    }

    override fun backClick() {
        router.onBack()
    }

    override fun createPost() {
        if (post == null) {
            create()
        } else {
            update()
        }
    }

    fun update() {
        val post = post ?: return
        communityFeedUseCase.updatePost(
            viewModelScope,
            post.id,
            createPostModel.description.observableField.get().orEmpty(),
            createPostModel.deletedItemsId.toTypedArray(),
            createPostModel.media,
            BaseCallback(
                onSuccess = {
                    result.postValue(it)
                    router.onBack()
                },
                onError = {
                    showPopupEvent.postValue(it)
                }
            )
        )
    }

    fun create() {
        communityFeedUseCase.createPost(
            viewModelScope,
            createPostModel.description.observableField.get().orEmpty(),
            createPostModel.media,
            BaseCallback(
                onSuccess = {
                    result.postValue(it)
                    router.onBack()
                },
                onError = {
                    showPopupEvent.postValue(it)
                }
            )
        )
    }

    override fun setEditMode(post: Post) {
        this.post = post
        createPostModel.description.observableField.set(post.description)
        createPostModel.media.addAll(post.attachments)
        attachments.postValue(createPostModel.media)
        canAddAttachments.value = createPostModel.media.size < ATTACHMENTS_COUNT
    }

    override fun processSingleItem(uri: String, mimeType: String) {
        if (createPostModel.media.size >= ATTACHMENTS_COUNT) return

        val index = createPostModel.media.indexOfFirst { it.url == uri }
        val newAttachment = Attachment(
            id = -1,
            contentType = getContentTypeFromMime(mimeType),
            url = uri,
            size = Size(0, 0),
            thumbnail = ""
        )
        if (index == -1) {
            createPostModel.media.add(newAttachment)
            canComplete.value = createPostModel.description.observableField.get()
                .orEmpty().length > 2 || createPostModel.media.size > 0
        } else createPostModel.media[index] = newAttachment
        canAddAttachments.value = createPostModel.media.size < ATTACHMENTS_COUNT
    }

    override fun deleteItemAt(pos: Int) {
        val item = createPostModel.media.removeAt(pos)
        canComplete.value = createPostModel.description.observableField.get()
            .orEmpty().length > 2 || createPostModel.media.size > 0
        if (item.id != -1L) {
            createPostModel.deletedItemsId.add(item.id)
        }
        attachments.value = createPostModel.media
        canAddAttachments.value = createPostModel.media.size < ATTACHMENTS_COUNT
    }

    companion object {
        const val ATTACHMENTS_COUNT = 5
    }
}