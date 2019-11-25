package com.doneit.ascend.presentation.main.base

import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import java.util.*


abstract class BaseActivity : com.vrgsoft.core.presentation.activity.BaseActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context?): Context? {
        if(context != null){
            val locale = Locale("EN")
            Locale.setDefault(locale)

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResourcesLocale(context, locale)
            } else {
                updateResourcesLocaleLegacy(context, locale)
            }
        }
        return context
    }

    override fun getResources(): Resources {
        return baseContext.resources
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}