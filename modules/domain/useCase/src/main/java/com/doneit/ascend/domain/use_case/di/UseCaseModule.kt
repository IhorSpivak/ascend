package com.doneit.ascend.domain.use_case.di

import com.doneit.ascend.domain.use_case.interactor.answer.AnswerInteractor
import com.doneit.ascend.domain.use_case.interactor.answer.AnswerUseCase
import com.doneit.ascend.domain.use_case.interactor.ascention_plan.AscensionInteractor
import com.doneit.ascend.domain.use_case.interactor.ascention_plan.AscensionUseCase
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentInteractor
import com.doneit.ascend.domain.use_case.interactor.attachment.AttachmentUseCase
import com.doneit.ascend.domain.use_case.interactor.cards.CardsInteractor
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.domain.use_case.interactor.chats.ChatInteractor
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
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
import org.kodein.di.generic.singleton

object UseCaseModule {
    fun get() = Kodein.Module("UseCaseModule") {
        bind<UserUseCase>() with singleton {
            UserInteractor(
                instance()
            )
        }

        bind<QuestionUseCase>() with singleton {
            QuestionInteractor(
                instance()
            )
        }

        bind<AnswerUseCase>() with singleton {
            AnswerInteractor(
                instance()
            )
        }

        bind<PageUseCase>() with singleton {
            PageInteractor(
                instance()
            )
        }

        bind<GroupUseCase>() with singleton {
            GroupInteractor(
                instance()
            )
        }

        bind<MasterMindUseCase>() with singleton {
            MasterMindInteractor(
                instance()
            )
        }

        bind<SearchUseCase>() with singleton {
            SearchInteractor(
                instance()
            )
        }

        bind<NotificationUseCase>() with singleton {
            NotificationInteractor(
                instance()
            )
        }

        bind<CardsUseCase>() with singleton {
            CardsInteractor(
                instance()
            )
        }

        bind<PurchaseUseCase>() with singleton {
            PurchaseInteractor(
                instance()
            )
        }

        bind<AttachmentUseCase>() with singleton {
            AttachmentInteractor(
                instance()
            )
        }

        bind<AscensionUseCase>() with singleton {
            AscensionInteractor(
                instance()
            )
        }

        bind<ChatUseCase>() with singleton {
            ChatInteractor(
                instance()
            )
        }
    }
}