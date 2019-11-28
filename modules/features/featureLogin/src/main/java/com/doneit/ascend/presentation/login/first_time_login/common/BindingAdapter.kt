package com.doneit.ascend.presentation.login.first_time_login.common

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.QuestionEntity

@BindingAdapter("app:setAdapter", "app:setAdapterData", requireAll = false)
fun setQuestionsAdapter(
    view: RecyclerView,
    adapter: QuestionsAdapter,
    results: LiveData<List<QuestionEntity>>
) {

    if (view.adapter is QuestionsAdapter) {
        results.value?.let {
            (view.adapter as QuestionsAdapter).updateData(it)
        }

        return
    }

    view.adapter = adapter
}