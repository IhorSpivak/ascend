package com.doneit.ascend.presentation.main.base

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.utils.ConnectionObserver
import com.doneit.ascend.presentation.utils.showNoConnectionDialog
import com.doneit.ascend.presentation.views.ConnectionSnackbar
import java.util.*


abstract class BaseActivity : com.vrgsoft.core.presentation.activity.BaseActivity() {

    private var noConnectionDialog: ConnectionSnackbar? = null
    private val connectionObserver: ConnectionObserver by lazy {
        ConnectionObserver(this@BaseActivity)
    }


    override fun onResume() {
        super.onResume()

        connectionObserver.networkStateChanged.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    protected open fun onNetworkStateChanged(hasConnection: Boolean){
        if (hasConnection) {
            noConnectionDialog?.dismiss()
        } else {
            noConnectionDialog =
                window.decorView.rootView.showNoConnectionDialog(getString(R.string.connecting))
        }
    }

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

    inline fun <reified VM : BaseViewModelImpl> vm(factory: ViewModelProvider.Factory): VM {
        return ViewModelProviders.of(this, factory)[VM::class.java]
    }
}