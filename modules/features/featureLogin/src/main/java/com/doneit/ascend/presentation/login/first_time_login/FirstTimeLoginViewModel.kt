package com.doneit.ascend.presentation.login.first_time_login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.user.AnswerEntity
import com.doneit.ascend.domain.entity.dto.AnswersDTO
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class FirstTimeLoginViewModel(
    private val questionUseCase: QuestionUseCase,
    private val answerUseCase: AnswerUseCase,
    private val router: FirstTimeLoginContract.Router
) : BaseViewModelImpl(), FirstTimeLoginContract.ViewModel {

    override val canComplete = MutableLiveData<Boolean>()
    override val questions = questionUseCase.getQuestionsList()
    private val questionsStates: MutableMap<Long, Boolean> = mutableMapOf()
    private val questionsAnswers: MutableMap<Long, AnswerEntity> = mutableMapOf()
    private val community = MutableLiveData<String>()

    override fun completeClick() {
        canComplete.postValue(false)

        viewModelScope.launch {
            val answers = questionsAnswers.values

            val requestEntity =
                answerUseCase.createAnswers(
                    AnswersDTO(
                        community = community.value!!,
                        answers = answers.toList()
                    )
                )

            canComplete.postValue(true)

            if (requestEntity.isSuccessful) {
                router.goToMain()
            }
        }
    }

    override fun setState(questionId: Long, isValid: Boolean) {
        questionsStates[questionId] = isValid

        updateCanComplete()
    }

    override fun setQuestionAnswer(questionId: Long, answer: String) {
        questionsAnswers[questionId] = AnswerEntity(
            questionId,
            answer,
            null
        )
    }

    override fun setSelectedAnswer(questionId: Long, optionId: Long) {
        questionsAnswers[questionId] = AnswerEntity(
            questionId,
            null,
            optionId
        )
    }

    override fun setCommunity(community: String) {
        this.community.postValue(community)
        updateCanComplete(true)
    }

    private fun updateCanComplete(isSelectedCommunity: Boolean = false) {
        var isFormValid = true

        val isCommunitySelected = community.value != null || isSelectedCommunity

        questionsStates.forEach { entry ->
            val questionId = entry.key
            val isQuestionValid = entry.value
            if (!isQuestionValid) {
                val answer = questionsAnswers[questionId]?.answer
                if (isCommunitySelected && !answer.isNullOrEmpty()) {
                    isFormValid = false
                }
                return@forEach
            }
        }

        canComplete.postValue(isFormValid && isCommunitySelected)
    }
}