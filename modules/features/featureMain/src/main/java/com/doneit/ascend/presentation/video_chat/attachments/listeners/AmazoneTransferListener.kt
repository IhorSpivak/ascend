package com.doneit.ascend.presentation.video_chat.attachments.listeners

import android.util.Log
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState

open class AmazoneTransferListener : TransferListener {

    override fun onError(id: Int, e: Exception) {
        Log.e("onError", "Error during upload: $id", e)
    }

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
        Log.e("progress", "upload: $id $bytesCurrent $bytesTotal")
    }

    override fun onStateChanged(id: Int, state: TransferState?) {
        Log.e("stateChanged", state?.name ?: "")
    }
}