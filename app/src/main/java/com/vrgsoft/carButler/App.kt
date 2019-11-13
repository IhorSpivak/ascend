package com.vrgsoft.carButler

import android.app.Application
import com.vrgsoft.retrofit.common.RetrofitConfig
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.generic.instance

class App : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(com.vrgsoft.carButler.di.AppModule.module(this@App))
    }
    override val kodeinTrigger = KodeinTrigger()
    private val interceptor: AuthCustomInterceptor by instance()

    override fun onCreate() {
        super.onCreate()

        val baseUrlValue = ""//todo
        RetrofitConfig.apply {
            baseUrl = baseUrlValue
            auth = interceptor
            enableLogging()
        }

        kodeinTrigger.trigger()
    }
}
