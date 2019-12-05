package com.doneit.ascend.presentation.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import com.doneit.ascend.domain.entity.UserEntity

class LocalStorage(
    private val context: Context,
    private val accountManager: AccountManager
) {
    private val sharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

//    fun saveSessionToken(token: String) {
//
//    }

    fun getSessionToken(): String {
        val accounts = accountManager.getAccountsByType(context.packageName)

        if (accounts.isNotEmpty()) {
            val account = accounts[0]
            return accountManager.blockingGetAuthToken(account, "Bearer", false)
        }

        return ""
    }

    fun saveUIReturnStep(step: UIReturnStep) {
        with(sharedPreferences.edit()) {
            putInt(ARG_UI_RETURN_STEP, step.ordinal)
            apply()
        }
    }

    fun getUIReturnStep(): UIReturnStep {
        return UIReturnStep.values()[sharedPreferences.getInt(ARG_UI_RETURN_STEP, 0)]
    }

    fun saveUser(user: UserEntity, token: String) {

        // save user token
        val account = Account(user.name, context.packageName)

        accountManager.addAccountExplicitly(account, ARG_AM_PASSWORD, null)
        accountManager.setAuthToken(account, "Bearer", token)

        with(sharedPreferences.edit()) {
            putString(ARG_USER_NAME, user.name)
            putString(ARG_USER_EMAIL, user.email)
            putString(ARG_USER_PHONE, user.phone)
            putString(ARG_USER_CREATED_AT, user.createdAt)
            putString(ARG_USER_UPDATED_AT, user.updatedAt)
            putInt(ARG_USER_RATING, user.rating)
            putString(ARG_USER_ROLE, user.role)
            putString(ARG_USER_COMMUNITY, user.community)
            apply()
        }
    }

    fun loadUser(): UserEntity {
        return UserEntity(
            sharedPreferences.getString(ARG_USER_NAME, ""),
            sharedPreferences.getString(ARG_USER_EMAIL, ""),
            sharedPreferences.getString(ARG_USER_PHONE, ""),
            sharedPreferences.getString(ARG_USER_CREATED_AT, ""),
            sharedPreferences.getString(ARG_USER_UPDATED_AT, ""),
            null,
            sharedPreferences.getInt(ARG_USER_RATING, 0),
            sharedPreferences.getString(ARG_USER_ROLE, Constants.TYPE_MASTER_MIND),
            sharedPreferences.getString(ARG_USER_COMMUNITY, "Recovery")
        )
    }

    companion object {
        private const val APP_PREFERENCES = "app_settings"

        private const val ARG_UI_RETURN_STEP = "app_ui_return_step"

        private const val ARG_USER_NAME = "arg_user_name"
        private const val ARG_USER_EMAIL = "arg_user_email"
        private const val ARG_USER_PHONE = "arg_user_phone"
        private const val ARG_USER_CREATED_AT = "arg_user_created_at"
        private const val ARG_USER_UPDATED_AT = "arg_user_updated_at"
        private const val ARG_USER_RATING = "arg_user_rating"
        private const val ARG_USER_ROLE = "arg_user_role"
        private const val ARG_USER_COMMUNITY = "arg_user_community"

        private const val ARG_AM_PASSWORD = "7ds9f87jF*J(SDFM(7"
    }
}