package com.doneit.ascend.presentation.main.base

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.doneit.ascend.presentation.main.R
import java.util.*


abstract class BaseActivity : com.vrgsoft.core.presentation.activity.BaseActivity() {

    private lateinit var progressDialog: Dialog

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDialog()
    }

    private fun initDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_progress, null, false)
        progressDialog = AlertDialog.Builder(this, R.style.ProgressDialogStyle)
            .setCancelable(false)
            .setView(dialogView)
            .create()
    }

    protected fun showProgress(isDialogShown: Boolean) {
        if(isDialogShown) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
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