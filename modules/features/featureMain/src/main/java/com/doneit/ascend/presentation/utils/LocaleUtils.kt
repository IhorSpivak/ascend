package com.doneit.ascend.presentation.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*


fun Context.getCurrentCountyISO(): String {
    var res = ""
    try {
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simCountry = tm.simCountryIso
        if (simCountry != null && simCountry.length === 2) { // SIM country code is available
            res = simCountry
        } else if (tm.phoneType !== TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
            val networkCountry = tm.networkCountryIso
            res = networkCountry
        }
    } catch (e: Exception) {
        val locale = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            resources.configuration.getSystemLocale()
        } else {
            resources.configuration.getSystemLocaleLegacy()
        }

        res = locale.country
    }

    return res
}

@Suppress("DEPRECATION")
fun Configuration.getSystemLocaleLegacy(): Locale {
    return this.locale
}

@TargetApi(Build.VERSION_CODES.N)
fun Configuration.getSystemLocale(): Locale {
    return this.locales.get(0)
}