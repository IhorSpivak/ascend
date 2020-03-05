package com.doneit.ascend.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.doneit.ascend.R
import com.doneit.ascend.domain.use_case.interactor.notification.NotificationUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.MainActivity
import com.doneit.ascend.presentation.utils.Constants
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

        //val title = p0.notification?.title!!
        //val content = p0.toIntent()!!.extras!!["gcm.notification.data"] as String
        //val content = p0.data;
        //var model = Gson().fromJson(content, PushEvent::class.java)
        //model = model.copy(title = title)
        p0.notification?.let {
            sendNotification(it.title.orEmpty(), p0.data.getValue("group_id").toLong())
        }
        useCase.notificationReceived(p0.toEntity())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        GlobalScope.launch {
            userUseCase.updateFirebase(p0)
        }
    }

    private fun sendNotification(messageTitle: String, id: Long) {

        //TODO: figure out how to show SplashActivity in case of background app start:
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.KEY_GROUP_ID, id)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = "ascend_n"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                //TODO: no icon for pushes:
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(messageTitle)
            .setContentText(null)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "ascend_notification",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}