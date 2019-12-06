package com.doneit.ascend.presentation.utils

import android.content.Context

class LocalStorage(
    private val context: Context
) {
    private val sharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun saveUIReturnStep(step: UIReturnStep) {
        with(sharedPreferences.edit()) {
            putInt(ARG_UI_RETURN_STEP, step.ordinal)
            apply()
        }
    }

    fun getUIReturnStep(): UIReturnStep {
        return UIReturnStep.values()[sharedPreferences.getInt(ARG_UI_RETURN_STEP, 0)]
    }

    companion object {
        private const val APP_PREFERENCES = "app_settings"

        private const val ARG_UI_RETURN_STEP = "app_ui_return_step"
    }
}