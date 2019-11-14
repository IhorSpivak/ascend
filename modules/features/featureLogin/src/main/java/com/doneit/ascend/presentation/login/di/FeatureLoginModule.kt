package com.doneit.ascend.presentation.login.di

import com.doneit.ascend.presentation.login.LogInActivity
import com.doneit.ascend.presentation.login.LogInContract
import com.doneit.ascend.presentation.login.LogInViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object FeatureLoginModule {
    fun get(activity: LogInActivity) = Kodein.Module("LogInActivity") {
        bind<LogInContract.ViewModel>() with provider {
            LogInViewModel(
                instance(),
                instance()
            )
        }
    }
}