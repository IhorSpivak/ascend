package com.doneit.ascend.source.storage.remote.di

import com.doneit.ascend.source.storage.remote.api.AnswerApi
import com.doneit.ascend.source.storage.remote.api.QuestionApi
import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.repository.answer.AnswerRepository
import com.doneit.ascend.source.storage.remote.repository.answer.IAnswerRepository
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
    }
}