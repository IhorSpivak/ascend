package com.doneit.ascend.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SessionModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context
}
