package com.doneit.ascend.source.storage.remote.di

import com.doneit.ascend.source.storage.remote.api.*
import com.doneit.ascend.source.storage.remote.repository.answer.AnswerRepository
import com.doneit.ascend.source.storage.remote.repository.answer.IAnswerRepository
import com.doneit.ascend.source.storage.remote.repository.cards.CardsRepository
import com.doneit.ascend.source.storage.remote.repository.cards.ICardsRepository
import com.doneit.ascend.source.storage.remote.repository.group.GroupRepository
import com.doneit.ascend.source.storage.remote.repository.group.IGroupRepository
import com.doneit.ascend.source.storage.remote.repository.group.socket.GroupSocketRepository
import com.doneit.ascend.source.storage.remote.repository.group.socket.IGroupSocketRepository
import com.doneit.ascend.source.storage.remote.repository.master_minds.IMasterMindRepository
import com.doneit.ascend.source.storage.remote.repository.master_minds.MasterMindRepository
import com.doneit.ascend.source.storage.remote.repository.notification.INotificationRepository
import com.doneit.ascend.source.storage.remote.repository.notification.NotificationRepository
import com.doneit.ascend.source.storage.remote.repository.page.IPageRepository
import com.doneit.ascend.source.storage.remote.repository.page.PageRepository
import com.doneit.ascend.source.storage.remote.repository.purchase.IPurchaseRepository
import com.doneit.ascend.source.storage.remote.repository.purchase.PurchasesRepository
import com.doneit.ascend.source.storage.remote.repository.question.IQuestionRepository
import com.doneit.ascend.source.storage.remote.repository.question.QuestionRepository
import com.doneit.ascend.source.storage.remote.repository.user.IUserRepository
import com.doneit.ascend.source.storage.remote.repository.user.UserRepository
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

object StorageRemoteModule {
    fun get() = Kodein.Module("StorageRemoteModule") {
        bind<Gson>() with singleton { Gson() }
        bind<UserApi>() with provider { instance<Retrofit>().create(UserApi::class.java) }
        bind<QuestionApi>() with provider { instance<Retrofit>().create(QuestionApi::class.java) }
        bind<AnswerApi>() with provider { instance<Retrofit>().create(AnswerApi::class.java) }
        bind<PageApi>() with provider { instance<Retrofit>().create(PageApi::class.java) }
        bind<GroupApi>() with provider { instance<Retrofit>().create(GroupApi::class.java) }
        bind<MasterMindApi>() with provider { instance<Retrofit>().create(MasterMindApi::class.java) }
        bind<NotificationApi>() with provider { instance<Retrofit>().create(NotificationApi::class.java) }
        bind<CardsApi>() with provider { instance<Retrofit>().create(CardsApi::class.java) }
        bind<PurchaseApi>() with provider { instance<Retrofit>().create(PurchaseApi::class.java) }

        bind<IUserRepository>() with provider {
            UserRepository(
                instance(),
                instance()
            )
        }
        bind<IQuestionRepository>() with provider {
            QuestionRepository(
                instance(),
                instance()
            )
        }
        bind<IAnswerRepository>() with provider {
            AnswerRepository(
                instance(),
                instance()
            )
        }

        bind<IPageRepository>() with provider {
            PageRepository(
                instance(),
                instance()
            )
        }

        bind<IGroupRepository>() with provider {
            GroupRepository(
                instance(),
                instance()
            )
        }

        bind<IMasterMindRepository>() with provider {
            MasterMindRepository(
                instance(),
                instance()

            )
        }

        bind<INotificationRepository>() with provider {
            NotificationRepository(
                instance(),
                instance()
            )
        }

        bind<ICardsRepository>() with provider {
            CardsRepository(
                instance(),
                instance()
            )
        }

        bind<IGroupSocketRepository>() with singleton {
            GroupSocketRepository(
                instance()
            )
        }

        bind<IPurchaseRepository>() with singleton {
            PurchasesRepository(
                instance(),
                instance()
            )
        }
    }
}