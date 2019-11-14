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
    private val interceptor: AuthCustomInterceptor by instance()

    override fun onCreate() {
        super.onCreate()

        RetrofitConfig.apply {
            baseUrl = getString(R.string.base_url)
            auth = interceptor
            enableLogging()
        }

        kodeinTrigger.trigger()
    }
}
