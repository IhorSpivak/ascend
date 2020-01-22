package com.doneit.ascend.presentation.login.first_time_login.common

import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.CommunityQuestionEntity
import com.doneit.ascend.domain.entity.QuestionListEntity

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

