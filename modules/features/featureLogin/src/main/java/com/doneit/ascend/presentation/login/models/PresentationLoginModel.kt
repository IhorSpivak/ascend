package com.doneit.ascend.presentation.login.models

class PresentationLoginModel(
    var phoneCode: String = "",
    var phone: String = "",
    var password: String = ""
) {
    fun getPhoneNumber(): String {
        return phoneCode + phone
    }
}