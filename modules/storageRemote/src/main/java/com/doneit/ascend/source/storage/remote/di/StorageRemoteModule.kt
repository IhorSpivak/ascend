package com.doneit.ascend.source.storage.remote.di

import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.repository.sample.IUserRepository
import com.doneit.ascend.source.storage.remote.repository.sample.UserRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

object StorageRemoteModule {
    fun get() = Kodein.Module("StorageRemoteModule") {
        bind<UserApi>() with provider { instance<Retrofit>().create(UserApi::class.java) }
        bind<IUserRepository>() with provider { UserRepository(instance()) }
    }
}