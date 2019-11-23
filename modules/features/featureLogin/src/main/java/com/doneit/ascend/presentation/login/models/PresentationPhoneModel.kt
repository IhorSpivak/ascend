package com.doneit.ascend.presentation.login.models

import androidx.databinding.ObservableField

class PresentationPhoneModel (
    var phoneCode: ObservableField<String> = ObservableField(""),
    var phone: ValidatableField = ValidatableField()
    ) {
    fun getPhoneNumber(): String {
        return phoneCode.get() + phone.observableField.get()
    }
}