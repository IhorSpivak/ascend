package com.doneit.ascend.domain.gateway.di

import com.doneit.ascend.domain.gateway.gateway.AnswerGateway
import com.doneit.ascend.domain.gateway.gateway.PageGateway
import com.doneit.ascend.domain.gateway.gateway.QuestionGateway
import com.doneit.ascend.domain.gateway.gateway.UserGateway
import com.doneit.ascend.domain.use_case.gateway.IAnswerGateway
import com.doneit.ascend.domain.use_case.gateway.IPageGateway
import com.doneit.ascend.domain.use_case.gateway.IQuestionGateway
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
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
    }
}