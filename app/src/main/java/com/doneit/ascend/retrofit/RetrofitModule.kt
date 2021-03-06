package com.doneit.ascend.retrofit

import android.content.Context
import com.doneit.ascend.BuildConfig
import com.doneit.ascend.retrofit.common.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitModule {
    fun get() = Kodein.Module("RetrofitModule") {
        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .baseUrl(RetrofitConfig.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(instance())
                .build()
        }

        bind<OkHttpClient>() with singleton {
            val builder = OkHttpClient.Builder()

            builder.cache(instance())



            builder.connectTimeout(100, TimeUnit.SECONDS)
            builder.retryOnConnectionFailure(true)
            builder.addInterceptor(instance<RedirectInterceptor>())
            builder.addInterceptor(instance<AuthInterceptor>())
            builder.addInterceptor(HeaderInterceptor())

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(ContentTypeInterceptor(loggingInterceptor))
                builder.addInterceptor(loggingInterceptor)
            }

            builder.build()
        }

        bind() from provider {
            val cacheSize = 10 * 1024 * 1024 // 10 MB
            Cache(instance<Context>().cacheDir, cacheSize.toLong())
        }

        bind<AuthInterceptor>() with singleton { AuthInterceptor(RetrofitConfig.auth) }

        bind<RedirectInterceptor>() with singleton { RedirectInterceptor(instance()) }
    }
}