package com.doneit.ascend.source.storage.remote.di

import com.doneit.ascend.source.storage.remote.api.*
import com.doneit.ascend.source.storage.remote.repository.answer.AnswerRepository
import com.doneit.ascend.source.storage.remote.repository.answer.IAnswerRepository
import com.doneit.ascend.source.storage.remote.repository.attachments.AttachmentRepository
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
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
import org.kodein.di.generic.singleton
import retrofit2.Retrofit

object StorageRemoteModule {
    fun get() = Kodein.Module("StorageRemoteModule") {
        bind<Gson>() with singleton { Gson() }
        bind<UserApi>() with singleton { instance<Retrofit>().create(UserApi::class.java) }
        bind<QuestionApi>() with singleton { instance<Retrofit>().create(QuestionApi::class.java) }
        bind<AnswerApi>() with singleton { instance<Retrofit>().create(AnswerApi::class.java) }
        bind<PageApi>() with singleton { instance<Retrofit>().create(PageApi::class.java) }
        bind<GroupApi>() with singleton { instance<Retrofit>().create(GroupApi::class.java) }
        bind<MasterMindApi>() with singleton { instance<Retrofit>().create(MasterMindApi::class.java) }
        bind<NotificationApi>() with singleton { instance<Retrofit>().create(NotificationApi::class.java) }
        bind<CardsApi>() with singleton { instance<Retrofit>().create(CardsApi::class.java) }
        bind<PurchaseApi>() with singleton { instance<Retrofit>().create(PurchaseApi::class.java) }
        bind<AttachmentsApi>() with singleton { instance<Retrofit>().create(AttachmentsApi::class.java) }

        bind<IUserRepository>() with singleton {
            UserRepository(
                instance(),
                instance()
            )
        }
        bind<IQuestionRepository>() with singleton {
            QuestionRepository(
                instance(),
                instance()
            )
        }
        bind<IAnswerRepository>() with singleton {
            AnswerRepository(
                instance(),
                instance()
            )
        }

        bind<IPageRepository>() with singleton {
            PageRepository(
                instance(),
                instance()
            )
        }

        bind<IGroupRepository>() with singleton {
            GroupRepository(
                instance(),
                instance(),
                instance()
            )
        }

        bind<IMasterMindRepository>() with singleton {
            MasterMindRepository(
                instance(),
                instance()

            )
        }

        bind<INotificationRepository>() with singleton {
            NotificationRepository(
                instance(),
                instance()
            )
        }

        bind<ICardsRepository>() with singleton {
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

        bind<IAttachmentsRepository>() with singleton {
            AttachmentRepository(
                instance(),
                instance()
            )
        }
    }
}