package com.doneit.ascend.presentation.models.community_feed

import androidx.annotation.StringRes
import com.doneit.ascend.presentation.main.R

enum class SharePostFilter(
    @StringRes val title: Int
) {
    CHAT(R.string.filter_chat),
    CHANNEL(R.string.filter_channel),
    USER(R.string.filter_user);
}