package com.doneit.ascend.presentation.video_chat.attachments.listeners

import com.hbisoft.pickit.PickiTCallbacks

open class PickiTListener: PickiTCallbacks {
    override fun PickiTonProgressUpdate(progress: Int) {
    }

    override fun PickiTonStartListener() {
    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
    }
}