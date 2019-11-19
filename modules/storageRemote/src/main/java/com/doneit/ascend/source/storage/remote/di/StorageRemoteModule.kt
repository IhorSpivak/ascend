package com.doneit.ascend.source.storage.remote.di

import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.repository.IUserRepository
import com.doneit.ascend.source.storage.remote.repository.UserRepository
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
        bind<IUserRepository>() with provider {
            UserRepository(
                instance(),
                instance()
            )
        }
    }
}