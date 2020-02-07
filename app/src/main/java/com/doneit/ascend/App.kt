package com.doneit.ascend

import android.app.Application
import android.util.Log
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.retrofit.common.RetrofitConfig
import com.google.firebase.FirebaseApp
import com.stripe.android.PaymentConfiguration
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
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

        val config: TwitterConfig = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG)) //enable logging when app is in debug mode
            .twitterAuthConfig(
                TwitterAuthConfig(
                    getString(R.string.twitter_consumer_key),
                    getString(R.string.twitter_consumer_secret)
                )
            )
            .debug(true)
            .build()

        Twitter.initialize(config)

        RetrofitConfig.apply {
            baseUrl = getString(R.string.base_url)
            auth = interceptor

            enableLogging()
        }

        if(BuildConfig.DEBUG) {
            PaymentConfiguration.init(applicationContext, Constants.STRIPE_KEY_TEST)
        } else {
            PaymentConfiguration.init(applicationContext, Constants.STRIPE_KEY_LIVE)
        }
        FirebaseApp.initializeApp(this)
        kodeinTrigger.trigger()
    }
}
