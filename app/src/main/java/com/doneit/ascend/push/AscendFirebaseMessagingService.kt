package com.doneit.ascend.push

import android.util.Log
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.push.data.PushEvent
import com.doneit.ascend.push.data.toEntity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class AscendFirebaseMessagingService : FirebaseMessagingService(), KodeinAware {

    private val _parentKodein by closestKodein()

    override val kodein: Kodein = Kodein.lazy {
        extend(_parentKodein)
    }

    override val kodeinTrigger = KodeinTrigger()

    override fun onCreate() {
        kodeinTrigger.trigger()
        super.onCreate()
    }


    private val useCase: NotificationUseCase by instance()

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d("ssd", Gson().toJson(p0))

        //todo refactor
        val title = p0.notification?.title!!
        val content = p0.toIntent()!!.extras!!["gcm.notification.data"] as String
        var model = Gson().fromJson(content, PushEvent::class.java)
        model = model.copy(title = title)

        useCase.notificationReceived(model.toEntity())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        //todo refresh token
    }

    companion object {
        private const val GROUP_ID_KEY = "group_id"
        private const val EVENT_TYPE_KEY = "type"
    }
}