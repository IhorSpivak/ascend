package com.vrgsoft.carButler.di

import android.accounts.AccountManager
import com.vrgsoft.carButler.App
import com.vrgsoft.carButler.AuthCustomInterceptor
import com.vrgsoft.carButler.domain.gateway.di.GatewayModule
import com.vrgsoft.carButler.domain.use_case.di.UseCaseModule
import com.vrgsoft.carButler.presentation.AppRouter
import com.vrgsoft.carButler.presentation.main.IMainRouter
import com.vrgsoft.carButler.presentation.splash.ISplashRouter
import com.vrgsoft.carButler.source.storage.local.di.StorageLocalModule
import com.vrgsoft.carButler.source.storage.remote.di.StorageRemoteModule
import com.vrgsoft.retrofit.RetrofitModule
import org.kodein.di.Kodein
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object AppModule {
    fun module(application: App) = Kodein.Module("AppModule") {
        import(androidModule(application))
        import(RetrofitModule.get())
        import(StorageRemoteModule.get())
        import(StorageLocalModule.get())
        import(UseCaseModule.get())
        import(GatewayModule.get())

        bind<ISplashRouter>() with singleton { AppRouter(application) }
        bind<IMainRouter>() with singleton { AppRouter(application) }
        bind<String>(tag = "appPackageName") with singleton { application.packageName }

        bind<AuthCustomInterceptor>() with provider {
            AuthCustomInterceptor(
                AccountManager.get(application),
                instance(tag = "appPackageName")
            )
        }
    }
}
