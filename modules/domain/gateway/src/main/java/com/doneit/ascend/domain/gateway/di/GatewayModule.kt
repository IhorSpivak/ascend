package com.doneit.ascend.domain.gateway.di

import com.doneit.ascend.domain.gateway.gateway.*
import com.doneit.ascend.domain.gateway.gateway.community_feed.CommunityFeedGateway
import com.doneit.ascend.domain.use_case.gateway.*
import com.doneit.ascend.source.storage.remote.repository.chats.socket.ChatSocketRepository
import com.doneit.ascend.source.storage.remote.repository.chats.socket.IChatSocketRepository
import com.doneit.ascend.source.storage.remote.repository.community_feed.socket.CommunityFeedSocketRepository
import com.doneit.ascend.source.storage.remote.repository.community_feed.socket.ICommunityFeedSocketRepository
import com.doneit.ascend.source.storage.remote.repository.group.questions_socket.IQuestionSocketRepository
import com.doneit.ascend.source.storage.remote.repository.group.questions_socket.QuestionSocketRepository
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
                instance(),
                instance()
            )
        }

        bind<IAscensionGateway>() with singleton {
            AscensionGateway(
                instance()
            )
        }
        bind<IChatSocketRepository>() with singleton {
            ChatSocketRepository(
                instance()
            )
        }

        bind<ICommunityFeedSocketRepository>() with singleton {
            CommunityFeedSocketRepository(
                instance()
            )
        }

        bind<IMyChatGateway>() with singleton {
            MyChatGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName"),
                instance(),
                instance()
            )
        }

        bind<IWebinarQuestionGateway>() with singleton {
            WebinarQuestionGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName"),
                instance()
            )
        }

        bind<IQuestionSocketRepository>() with singleton {
            QuestionSocketRepository(
                instance()
            )
        }

        bind<IVimeoGateway>() with singleton {
            VimeoGateway(
                instance(),
                instance()
            )
        }

        bind<ICommunityFeedGateway>() with singleton {
            CommunityFeedGateway(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(tag = "appPackageName")
            )
        }
    }
}