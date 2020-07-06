package com.doneit.ascend.source.storage.local.di

import androidx.room.Room
import com.doneit.ascend.source.storage.local.repository.LocalDatabase
import com.doneit.ascend.source.storage.local.repository.attachments.AttachmentRepository
import com.doneit.ascend.source.storage.local.repository.attachments.IAttachmentRepository
import com.doneit.ascend.source.storage.local.repository.chats.IMyChatsRepository
import com.doneit.ascend.source.storage.local.repository.chats.MyChatsRepository
import com.doneit.ascend.source.storage.local.repository.community_feed.CommunityFeedRepository
import com.doneit.ascend.source.storage.local.repository.community_feed.ICommunityFeedRepository
import com.doneit.ascend.source.storage.local.repository.groups.GroupRepository
import com.doneit.ascend.source.storage.local.repository.groups.IGroupRepository
import com.doneit.ascend.source.storage.local.repository.master_minds.IMasterMindRepository
import com.doneit.ascend.source.storage.local.repository.master_minds.MasterMindRepository
import com.doneit.ascend.source.storage.local.repository.notification.INotificationRepository
import com.doneit.ascend.source.storage.local.repository.notification.NotificationRepository
import com.doneit.ascend.source.storage.local.repository.question.IQuestionRepository
import com.doneit.ascend.source.storage.local.repository.question.QuestionRepository
import com.doneit.ascend.source.storage.local.repository.user.IUserRepository
import com.doneit.ascend.source.storage.local.repository.user.UserRepository
import com.doneit.ascend.source.storage.local.repository.webinar_question.IWebinarQuestionRepository
import com.doneit.ascend.source.storage.local.repository.webinar_question.WebinarQuestionRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

object StorageLocalModule {
    fun get() = Kodein.Module("StorageLocalModule") {
        bind<LocalDatabase>() with singleton {
            Room.databaseBuilder(instance(), LocalDatabase::class.java, LocalDatabase.NAME)
                .build()
        }

        bind<IQuestionRepository>() with singleton { QuestionRepository(instance()) }
        bind<IUserRepository>() with singleton { UserRepository(instance()) }
        bind<IMasterMindRepository>() with singleton { MasterMindRepository(instance<LocalDatabase>().masterMindDao()) }
        bind<IGroupRepository>() with singleton { GroupRepository(instance<LocalDatabase>().groupDao()) }
        bind<IAttachmentRepository>() with singleton { AttachmentRepository(instance<LocalDatabase>().attachmentDao()) }
        bind<INotificationRepository>() with singleton { NotificationRepository(instance<LocalDatabase>().notificationDao()) }
        bind<IMyChatsRepository>() with singleton { MyChatsRepository(instance<LocalDatabase>().myChatsDao()) }
        bind<IWebinarQuestionRepository>() with singleton { WebinarQuestionRepository(instance<LocalDatabase>().webinarQuestionDao()) }
        bind<ICommunityFeedRepository>() with singleton { CommunityFeedRepository(instance<LocalDatabase>().communityFeedDao()) }
    }
}