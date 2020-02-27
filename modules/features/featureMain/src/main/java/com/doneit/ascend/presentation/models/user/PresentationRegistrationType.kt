package com.doneit.ascend.presentation.models.user

import androidx.annotation.DrawableRes
import com.doneit.ascend.presentation.main.R

enum class PresentationRegistrationType(
    @DrawableRes val icon: Int?
) {
    FACEBOOK(R.drawable.ic_facebook),
    GOOGLE(R.drawable.ic_google),
    TWITTER(R.drawable.ic_twitter),
    REGULAR(null);

    override fun toString(): String {
        return super.toString().toLowerCase().capitalize()
    }
}