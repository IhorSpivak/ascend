package com.doneit.ascend.di

import android.accounts.AccountManager
import com.doneit.ascend.AuthCustomInterceptor
import com.doneit.ascend.domain.gateway.di.GatewayModule
import com.doneit.ascend.domain.use_case.di.UseCaseModule
import com.doneit.ascend.presentation.AppRouter
import com.doneit.ascend.presentation.login.LogInContract
import com.doneit.ascend.presentation.main.IMainRouter
import com.doneit.ascend.presentation.splash.ISplashRouter
import com.doneit.ascend.source.storage.local.di.StorageLocalModule
import com.doneit.ascend.source.storage.remote.di.StorageRemoteModule
import com.vrgsoft.retrofit.RetrofitModule
import org.kodein.di.Kodein
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object AppModule {
    fun module(application: com.doneit.ascend.App) = Kodein.Module("AppModule") {
        import(androidModule(application))
        import(RetrofitModule.get())
        import(StorageRemoteModule.get())
        import(StorageLocalModule.get())
        import(UseCaseModule.get())
        import(GatewayModule.get())

        bind<ISplashRouter>() with singleton {
            AppRouter(
                application
            )
        }
        bind<IMainRouter>() with singleton { AppRouter(application) }
        bind<LogInContract.Router>() with singleton { AppRouter(application) }
        bind<String>(tag = "appPackageName") with singleton { application.packageName }

        bind<AuthCustomInterceptor>() with provider {
            AuthCustomInterceptor(
                AccountManager.get(application),
                instance(tag = "appPackageName")
            )
        }
    }
}