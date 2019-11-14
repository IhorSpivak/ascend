package com.doneit.ascend

import android.app.Application
import com.vrgsoft.retrofit.common.RetrofitConfig
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.generic.instance

class App : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(com.doneit.ascend.di.AppModule.module(this@App))
    }
    override val kodeinTrigger = KodeinTrigger()
    private val interceptor: com.doneit.ascend.AuthCustomInterceptor by instance()

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
