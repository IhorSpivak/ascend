package com.doneit.ascend.presentation.main.home.community_feed.create_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreatePostModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class CreatePostViewModel(

) : BaseViewModelImpl(), CreatePostContract.ViewModel {
    override val createPostModel: PresentationCreatePostModel = PresentationCreatePostModel()
    override val canComplete: LiveData<Boolean> = MutableLiveData(false)

    override fun backClick() {
    }

}