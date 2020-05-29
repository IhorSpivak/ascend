package com.doneit.ascend.retrofit.vimeo

import com.doneit.ascend.retrofit.common.HeaderInterceptor
import com.doneit.ascend.retrofit.common.RetrofitConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object VimeoRetrofitModule {
    fun get() = Kodein.Module("VimeoRetrofitModule") {
        bind<Retrofit>("VimeoRetrofitModule") with singleton {
            Retrofit.Builder()
                .baseUrl("https://api.vimeo.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(instance("VimeoOkHttpClient"))
                .build()
        }

        bind<OkHttpClient>("VimeoOkHttpClient") with singleton {
            val builder = OkHttpClient.Builder()

            builder.cache(instance())



            builder.connectTimeout(100, TimeUnit.SECONDS)
            builder.retryOnConnectionFailure(true)
            builder.addInterceptor(instance<VimeoAuthInterceptor>())
            builder.addInterceptor(HeaderInterceptor())

            if (RetrofitConfig.enableLogging) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }

            builder.build()
        }


        //todo refactor token usage
        bind<VimeoAuthInterceptor>() with singleton { VimeoAuthInterceptor() }
    }
}