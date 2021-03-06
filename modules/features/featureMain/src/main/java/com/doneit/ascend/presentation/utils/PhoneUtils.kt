package com.doneit.ascend.presentation.utils

import android.content.Context
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
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

fun isPhoneValid(code: String, number: String): Boolean {
    val phoneUtil = PhoneNumberUtil.getInstance()

    return try {
        val phoneCode = code.toNumericCode()
        val isoCode = phoneUtil.getRegionCodeForCountryCode(Integer.parseInt(phoneCode))
        val numberProto = phoneUtil.parse(code + number, isoCode)
        phoneUtil.isValidNumber(numberProto)

    } catch (e: Exception) {
        false
    }
}

fun String.getCountyCode(): String {
    var res = ""

    val phoneUtil = PhoneNumberUtil.getInstance()
    try {
        // phone must begin with '+'
        val numberProto = phoneUtil.parse(this, null)
        res = numberProto.countryCode.toString()
        res = "+$res"
    } catch (e: NumberParseException) {
        e.printStackTrace()
    }

    return res
}

fun String.getPhoneBody(): String {
    var res = ""

    val phoneUtil = PhoneNumberUtil.getInstance()
    try {
        // phone must begin with '+'
        val numberProto = phoneUtil.parse(this, null)
        res = numberProto.nationalNumber.toString()
    } catch (e: NumberParseException) {
        e.printStackTrace()
    }

    return res
}

fun String.toNumericCode(): String {
    return if(startsWith('+')) {
        substring(1)
    } else {
        this
    }
}