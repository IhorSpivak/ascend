package com.doneit.ascend.presentation.login.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PresentationSignUpModel(
    var email: String = "",
    var phoneCode: String = "",
    var phone: String = "",
    var name: String = "",
    var password: String = "",
    var passwordConfirmation: String = "",
    var code: String = ""
): Parcelable
