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
                instance()
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
                instance()
            )
        }
    }
}