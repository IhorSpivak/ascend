package com.doneit.ascend.presentation.login.first_time_login

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.first_time_login.common.FirstTimeLoginArgs
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class FirstTimeLoginViewModel(
    private val userUseCase: UserUseCase,
    private val answerUseCase: AnswerUseCase,
    private val router: FirstTimeLoginContract.Router
) : BaseViewModelImpl(), FirstTimeLoginContract.ViewModel {

    override val canComplete = MutableLiveData<Boolean>()
    override val questions = MutableLiveData<List<QuestionEntity>>()

    override fun removeErrors() {

    }

    override fun completeClick() {

    }

    override fun applyArguments(args: FirstTimeLoginArgs) {
        questions.postValue(args.questions)
    }

    override fun setState(questionId: Long, isValid: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}