package com.doneit.ascend.source.storage.local.di

import androidx.room.Room
import com.doneit.ascend.source.storage.local.repository.LocalDatabase
import com.doneit.ascend.source.storage.local.repository.groups.GroupRepository
import com.doneit.ascend.source.storage.local.repository.groups.IGroupRepository
import com.doneit.ascend.source.storage.local.repository.master_minds.IMasterMindRepository
import com.doneit.ascend.source.storage.local.repository.master_minds.MasterMindRepository
import com.doneit.ascend.source.storage.local.repository.question.IQuestionRepository
import com.doneit.ascend.source.storage.local.repository.question.QuestionRepository
import com.doneit.ascend.source.storage.local.repository.user.IUserRepository
import com.doneit.ascend.source.storage.local.repository.user.UserRepository
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
    }
}