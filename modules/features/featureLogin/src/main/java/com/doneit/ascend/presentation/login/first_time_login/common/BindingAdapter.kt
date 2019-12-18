package com.doneit.ascend.presentation.login.first_time_login.common

import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.CommunityQuestionEntity
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.TemplateAnswerItemBinding
import com.doneit.ascend.presentation.login.databinding.TemplateSelectAnswerItemBinding

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setQuestionsAdapter(
    view: RecyclerView,
    adapter: QuestionsAdapter,
    results: LiveData<QuestionListEntity>
) {

    if (view.adapter is QuestionsAdapter) {
        results.value?.let {
            (view.adapter as QuestionsAdapter).updateData(it)
        }

        return
    }

    view.adapter = adapter
}

@BindingAdapter("app:setQuestion", "app:setListener", requireAll = true)
fun setQuestionsAdapter(
    view: LinearLayout,
    question: CommunityQuestionEntity,
    listener: QuestionStateListener
) {

}

