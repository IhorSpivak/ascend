package com.doneit.ascend.domain.entity.user

import androidx.annotation.StringRes
import com.doneit.ascend.domain.entity.R

enum class Community(
    val title: String,
    @StringRes val resId: Int
) {
    RECOVERY("Recovery", R.string.recovery),
    LIFESTYLE("Lifestyle", R.string.lifestyle),
    INDUSTRY("Industry", R.string.industry),
    FAMILY("Family", R.string.family),
    SUCCESS("Success", R.string.success),
    SPIRITUAL("Spiritual", R.string.spiritual);


    override fun toString(): String {
        return title
    }

}