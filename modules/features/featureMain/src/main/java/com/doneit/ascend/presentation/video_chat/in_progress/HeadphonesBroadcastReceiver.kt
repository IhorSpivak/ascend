package com.doneit.ascend.presentation.video_chat.in_progress

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HeadphonesBroadcastReceiver(
    private val onStateChanged: (Boolean) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_HEADSET_PLUG) {
                val state = it.getIntExtra("state", -1)
                val res = when (state) {
                    0 -> true
                    1 -> false
                    else -> true
                }
                onStateChanged.invoke(res)
            }
        }
    }
}