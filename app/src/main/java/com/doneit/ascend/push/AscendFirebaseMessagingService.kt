package com.doneit.ascend.push

import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.push.data.PushEvent
import com.doneit.ascend.push.data.toEntity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    private val userUseCase: UserUseCase by instance()

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        //todo refactor
        /*p0.notification?.let {
            sendNotification(it.body.orEmpty(), it.title.orEmpty(), p0.data)
        }*/
        val title = p0.notification?.title!!
        val content = p0.toIntent()!!.extras!!["gcm.notification.data"] as String
        var model = Gson().fromJson(content, PushEvent::class.java)
        model = model.copy(title = title)

        useCase.notificationReceived(model.toEntity())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        //todo refresh token
        GlobalScope.launch {
            userUseCase.updateFirebase(p0)
        }
    }
}