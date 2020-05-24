package com.doneit.ascend.presentation.video_chat_webinar.questions

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.WebinarQuestionEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface QuestionContract{
    interface ViewModel : BaseViewModel {
        val questions: LiveData<PagedList<WebinarQuestionEntity>>
    }
}