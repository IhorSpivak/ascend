package com.doneit.ascend.presentation.video_chat_webinar.preview

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatActivity
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent

interface WebinarChatPreviewContract {
    interface ViewModel : BaseViewModel {
        val groupInfo: LiveData<GroupEntity>
        val isStartButtonVisible: LiveData<Boolean>
        val credentials: LiveData<StartWebinarVideoModel>
        val isVisitor: LiveData<Boolean>
        val isQuestionSent: LiveData<Boolean>
        val questions: LiveData<PagedList<WebinarQuestionEntity>>
        val showMessgeSent: SingleLiveEvent<Void>

        fun onOpenOptions()
        fun createQuestion(question: String)
        fun onStartGroupClick()
        fun onPermissionsRequired(resultCode: WebinarVideoChatActivity.ResultStatus)
    }
}