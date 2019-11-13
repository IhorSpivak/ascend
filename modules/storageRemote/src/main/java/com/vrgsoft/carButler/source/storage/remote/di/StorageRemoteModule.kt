package com.vrgsoft.carButler.source.storage.remote.di

import com.vrgsoft.carButler.source.storage.remote.api.SampleApi
import com.vrgsoft.carButler.source.storage.remote.repository.sample.ISampleRepository
import com.vrgsoft.carButler.source.storage.remote.repository.sample.SampleRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

object StorageRemoteModule {
    fun get() = Kodein.Module("StorageRemoteModule") {
        bind<SampleApi>() with provider { instance<Retrofit>().create(SampleApi::class.java) }
        bind<ISampleRepository>() with provider { SampleRepository(instance()) }
    }
}