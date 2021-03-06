package com.doneit.ascend.di

import android.accounts.AccountManager
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.doneit.ascend.AuthCustomInterceptor
import com.doneit.ascend.domain.gateway.di.GatewayModule
import com.doneit.ascend.domain.use_case.di.UseCaseModule
import com.doneit.ascend.presentation.AppRouter
import com.doneit.ascend.presentation.IMainAppRouter
import com.doneit.ascend.presentation.login.ILogInAppRouter
import com.doneit.ascend.presentation.splash.ISplashRouter
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.retrofit.RetrofitModule
import com.doneit.ascend.retrofit.vimeo.VimeoRetrofitModule
import com.doneit.ascend.source.storage.local.di.StorageLocalModule
import com.doneit.ascend.source.storage.remote.di.StorageRemoteModule
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
        import(VimeoRetrofitModule.get())
        import(StorageRemoteModule.get())
        import(StorageLocalModule.get())
        import(UseCaseModule.get())
        import(GatewayModule.get())

        bind<ISplashRouter>() with singleton {
            AppRouter(
                application
            )
        }
        bind<IMainAppRouter>() with singleton { AppRouter(application) }
        bind<ILogInAppRouter>() with singleton { AppRouter(application) }
        bind<String>(tag = "appPackageName") with singleton { application.packageName }

        bind<AuthCustomInterceptor>() with provider {
            AuthCustomInterceptor(
                AccountManager.get(application),
                instance(tag = "appPackageName")
            )
        }

        bind<CognitoCachingCredentialsProvider>() with singleton {
            CognitoCachingCredentialsProvider(
                application.applicationContext,
                Constants.COGNITO_POOL_ID,
                Regions.fromName(Constants.COGNITO_POOL_REGION)
            )
        }
    }
}
