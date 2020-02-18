package com.doneit.ascend.presentation.video_chat.attachments

import androidx.annotation.StringRes
import com.doneit.ascend.presentation.main.R

enum class TransferEvent(
    @StringRes val messageRes: Int
) {
    STARTED(R.string.transfer_started),
    COMPLETED(R.string.transfer_completed),
    ERROR(R.string.transfer_error)
}