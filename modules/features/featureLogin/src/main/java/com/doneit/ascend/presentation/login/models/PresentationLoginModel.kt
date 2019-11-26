package com.doneit.ascend.presentation.login.models

import androidx.databinding.ObservableField

class PresentationLoginModel(
    var phoneCode: ObservableField<String> = ObservableField(""),
    var phone: ValidatableField = ValidatableField(),
    var password: String = ""
) {
    fun getPhoneNumber(): String {
        return phoneCode.get() + phone.observableField.get()
    }
}