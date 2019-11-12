package com.doneit.ascend

import android.app.Application
import android.util.Log
import com.doneit.ascend.di.AppComponent
import com.doneit.ascend.di.DaggerAppComponent
import com.doneit.ascend.di.NetworkModule
import com.doneit.ascend.di.SessionModule
import io.reactivex.plugins.RxJavaPlugins

object Injector {
    lateinit var appComponent: AppComponent
}

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Injector.appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .sessionModule(SessionModule(this))
            .build()

        RxJavaPlugins.setErrorHandler {
            Log.e(App::class.java.simpleName, it.message)
        }
    }
}
