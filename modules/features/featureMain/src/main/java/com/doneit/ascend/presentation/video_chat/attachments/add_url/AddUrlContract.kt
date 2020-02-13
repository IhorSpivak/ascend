package com.doneit.ascend.presentation.video_chat.attachments.add_url

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsContract

interface AddUrlContract {
    interface ViewModel : BaseViewModel {
        val navigation: LiveData<AttachmentsContract.Navigation>
        val canSave : MutableLiveData<Boolean>

        fun backClick()
        fun onAddUrlClick()
    }

    interface Router {
        fun onBack()
    }

    enum class Navigation {
        BACK
    }
}