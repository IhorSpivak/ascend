package com.doneit.ascend.presentation.login.utils

import android.content.Context
import com.rilixtech.widget.countrycodepicker.Country

fun fetchCountryListWithReflection(context: Context): List<Country> {
    return try {
        val utils = Class.forName("com.rilixtech.widget.countrycodepicker.CountryUtils")
        val contextClass = Class.forName("android.content.Context")
        val method = utils.getDeclaredMethod("getAllCountries", contextClass)
        method.isAccessible = true
        method.invoke(null, context) as List<Country>
    } catch (e: java.lang.Exception) {
        listOf()
    }
}