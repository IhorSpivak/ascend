package com.doneit.ascend.domain.use_case.di

import com.doneit.ascend.domain.use_case.interactor.answer.AnswerInteractor
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.question.QuestionInteractor
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserInteractor
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object UseCaseModule {
    fun get() = Kodein.Module("UseCaseModule") {
        bind<UserUseCase>() with provider {
            UserInteractor(
                instance()
            )
        }

        bind<QuestionUseCase>() with provider {
            QuestionInteractor(
                instance()
            )
        }

        bind<AnswerUseCase>() with provider {
            AnswerInteractor(
                instance()
            )
        }
    }
}