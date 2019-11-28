package com.doneit.ascend.presentation.login.first_time_login

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.presentation.login.first_time_login.common.FirstTimeLoginArgs
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.LocalStorage
import com.doneit.ascend.presentation.utils.UIReturnStep
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class FirstTimeLoginViewModel(
    private val questionUseCase: QuestionUseCase,
    private val answerUseCase: AnswerUseCase,
    private val router: FirstTimeLoginContract.Router,
    private val localStorage: LocalStorage
) : BaseViewModelImpl(), FirstTimeLoginContract.ViewModel {

    override val canComplete = MutableLiveData<Boolean>()
    override val questions = MutableLiveData<List<QuestionEntity>>()
    private val questionsStates: MutableMap<Long, Boolean> = mutableMapOf()
    private val questionsAnswers: MutableMap<Long, AnswerEntity> = mutableMapOf()

    override fun completeClick() {
        canComplete.postValue(false)

        GlobalScope.launch {
            val answers = questionsAnswers.values

            val requestEntity =
                answerUseCase.createAnswers(
                    localStorage.getSessionToken(),
                    answers.toList()
                )

            canComplete.postValue(true)

            if (requestEntity.isSuccessful) {
                try {
                    localStorage.saveUIReturnStep(UIReturnStep.NONE)
                    questionUseCase.deleteAllQuestions()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    router.goToMain()
                }
            }
        }
    }

    override fun applyArguments(args: FirstTimeLoginArgs) {
        questions.postValue(args.questions)
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

    private fun updateCanComplete() {
        var isFormValid = true

        questionsStates.values.forEach {
            if (!it) {
                isFormValid = false
                return@forEach
            }
        }

        canComplete.postValue(isFormValid)
    }
}