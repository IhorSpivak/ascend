package com.doneit.ascend.di

import com.doneit.ascend.networking.API
import com.doneit.ascend.networking.interceptors.AuthInterceptor
import com.doneit.ascend.networking.interceptors.ErrorInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        authInterceptor: AuthInterceptor,
        errorInterceptor: ErrorInterceptor
    ): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            )
            .addInterceptor(errorInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://ascend-backend.herokuapp.com/api/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPI(retrofit: Retrofit) = retrofit.create(API::class.java)

}
