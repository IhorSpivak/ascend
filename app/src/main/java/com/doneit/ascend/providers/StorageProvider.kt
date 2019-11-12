package com.doneit.ascend.providers

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

class StorageProvider @Inject constructor(context: Context) {


    private val sharedPreferences = context.getSharedPreferences("ascend", MODE_PRIVATE)

    var token = ""
        set(value) {
            field = value
            sharedPreferences.edit()
                .putString("token", value)
                .apply()
        }
        get() = sharedPreferences.getString("token", "")!!

    fun clearToken() = sharedPreferences.edit().remove("token").apply()


}
