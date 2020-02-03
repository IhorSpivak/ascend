package com.doneit.ascend.domain.gateway.di

import com.doneit.ascend.domain.gateway.gateway.*
import com.doneit.ascend.domain.use_case.gateway.*
import com.vrgsoft.networkmanager.NetworkManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object GatewayModule {
    fun get() = Kodein.Module("GatewayModule") {
        bind() from singleton { NetworkManager() }

        bind<IUserGateway>() with singleton {
            UserGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName")
            )
        }

        bind<IQuestionGateway>() with singleton {
            QuestionGateway(
                instance(),
                instance(),
                instance()
            )
        }

        bind<IAnswerGateway>() with singleton {
            AnswerGateway(
                instance(),
                instance(),
                instance()
            )
        }

        bind<IPageGateway>() with singleton {
            PageGateway(
                instance(),
                instance()
            )
        }

        bind<IGroupGateway>() with singleton {
            GroupGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName")
            )
        }

        bind<IMasterMindGateway>() with singleton {
            MasterMindGateway(
                instance(),
                instance(),
                instance()
            )
        }

        bind<ISearchGateway>() with singleton {
            SearchGateway(
                instance(),
                instance(),
                instance()
            )
        }

        bind<INotificationGateway>() with singleton {
            NotificationGateway(
                instance(),
                instance()
            )
        }

        bind<ICardsGateway>() with singleton {
            CardsGateway(
                instance(),
                instance()
            )
        }

        bind<IPurchaseGateway>() with singleton {
            PurchasesGateway(
                instance(),
                instance()
            )
        }

        bind<IAttachmentGateway>() with singleton {
            AttachmentGateway(
                instance(),
                instance()
            )
        }
    }
}