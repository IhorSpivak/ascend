package com.doneit.ascend.domain.gateway.di

import com.doneit.ascend.domain.gateway.gateway.*
import com.doneit.ascend.domain.use_case.gateway.*
import com.vrgsoft.networkmanager.NetworkManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object GatewayModule {
    fun get() = Kodein.Module("GatewayModule") {
        bind() from singleton { NetworkManager() }

        bind<IUserGateway>() with provider {
            UserGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName")
            )
        }

        bind<IQuestionGateway>() with provider {
            QuestionGateway(
                instance(),
                instance(),
                instance()
            )
        }

        bind<IAnswerGateway>() with provider {
            AnswerGateway(
                instance(),
                instance()
            )
        }

        bind<IPageGateway>() with provider {
            PageGateway(
                instance(),
                instance()
            )
        }

        bind<IGroupGateway>() with provider {
            GroupGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName")
            )
        }

        bind<IMasterMindGateway>() with provider {
            MasterMindGateway(
                instance(),
                instance()
            )
        }

        bind<ISearchGateway>() with provider {
            SearchGateway(
                instance(),
                instance(),
                instance()
            )
        }

        bind<INotificationGateway>() with provider {
            NotificationGateway(
                instance(),
                instance()
            )
        }

        bind<ICardsGateway>() with provider {
            CardsGateway(
                instance(),
                instance()
            )
        }

        bind<IPurchaseGateway>() with provider {
            PurchasesGateway(
                instance(),
                instance()
            )
        }
    }
}