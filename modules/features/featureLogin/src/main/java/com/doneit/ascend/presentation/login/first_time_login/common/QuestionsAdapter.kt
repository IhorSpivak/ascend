package com.doneit.ascend.presentation.login.first_time_login.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.presentation.login.first_time_login.common.view_holder.QuestionViewHolder
import com.doneit.ascend.presentation.login.first_time_login.common.view_holder.SelectAnswerViewHolder
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder

class QuestionsAdapter(
    private val items: MutableList<QuestionEntity>,
    private val listener: QuestionStateListener
) : RecyclerView.Adapter<LifecycleViewHolder>() {

    private var questionModel: QuestionListEntity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifecycleViewHolder {

        return when (QuestionType.values()[viewType]) {
            QuestionType.QUESTION -> QuestionViewHolder.create(parent, listener)
            QuestionType.SELECT_ANSWER -> SelectAnswerViewHolder.create(parent, listener)
            else -> QuestionViewHolder.create(parent, listener)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LifecycleViewHolder, position: Int) {

        when (QuestionType.values()[holder.itemViewType]) {
            QuestionType.QUESTION -> (holder as QuestionViewHolder).bind(items[position])
            //QuestionType.SELECT_ANSWER -> (holder as SelectAnswerViewHolder).bind(items[position])
            else -> (holder as QuestionViewHolder).bind(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {

        val question = items[position]
        val type = getQuestionType(question.type)

        return type.ordinal
    }

    private fun getQuestionType(questionType: String): QuestionType {
        return when (questionType) {
            "question" -> QuestionType.QUESTION
            "select_answer" -> QuestionType.SELECT_ANSWER
            else -> QuestionType.UNDEFINED
        }
    }

    fun updateData(questionModel: QuestionListEntity) {
        this.questionModel = questionModel
        val newItems = questionModel.questions

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }

            override fun getOldListSize() = items.size

            override fun getNewListSize() = newItems.size
        })

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }

    override fun onViewRecycled(holder: LifecycleViewHolder) {
        holder.unbind()
        super.onViewRecycled(holder)
    }
}