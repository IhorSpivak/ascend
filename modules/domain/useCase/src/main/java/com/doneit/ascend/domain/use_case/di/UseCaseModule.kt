package com.doneit.ascend.domain.use_case.di

import com.doneit.ascend.domain.use_case.interactor.answer.AnswerInteractor
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.cards.CardsInteractor
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupInteractor
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindInteractor
import com.doneit.ascend.domain.use_case.interactor.master_mind.MasterMindUseCase
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationInteractor
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.domain.use_case.interactor.page.PageInteractor
import com.doneit.ascend.domain.use_case.interactor.page.PageUseCase
import com.doneit.ascend.domain.use_case.interactor.purchase.PurchaseInteractor
import com.doneit.ascend.domain.use_case.interactor.purchase.PurchaseUseCase
import com.doneit.ascend.domain.use_case.interactor.question.QuestionInteractor
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.search.SearchInteractor
import com.doneit.ascend.domain.use_case.interactor.search.SearchUseCase
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

        bind<PageUseCase>() with provider {
            PageInteractor(
                instance()
            )
        }

        bind<GroupUseCase>() with provider {
            GroupInteractor(
                instance()
            )
        }

        bind<MasterMindUseCase>() with provider {
            MasterMindInteractor(
                instance()
            )
        }

        bind<SearchUseCase>() with provider {
            SearchInteractor(
                instance()
            )
        }

        bind<NotificationUseCase>() with provider {
            NotificationInteractor(
                instance()
            )
        }

        bind<CardsUseCase>() with provider {
            CardsInteractor(
                instance()
            )
        }

        bind<PurchaseUseCase>() with provider {
            PurchaseInteractor(
                instance()
            )
        }
    }
}