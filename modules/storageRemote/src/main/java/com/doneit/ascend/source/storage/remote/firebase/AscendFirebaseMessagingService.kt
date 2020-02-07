package com.doneit.ascend.source.storage.remote.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AscendFirebaseMessagingService : FirebaseMessagingService() {



    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("ssd", p0.toString())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}