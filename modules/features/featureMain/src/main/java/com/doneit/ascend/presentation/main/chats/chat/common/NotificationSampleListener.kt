package com.doneit.ascend.presentation.main.chats.chat.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.doneit.ascend.presentation.main.R
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.SpeedCalculator
import com.liulishuo.okdownload.core.breakpoint.BlockInfo
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend.Listener4SpeedModel

class NotificationSampleListener(context: Context) : DownloadListener4WithSpeed() {
    private lateinit var builder: NotificationCompat.Builder
    private var totalLength = 0
    private var manager: NotificationManager? = null
    private var taskEndRunnable: Runnable? = null
    private val context: Context = context.applicationContext
    private var action: NotificationCompat.Action? = null

    fun attachTaskEndRunnable(taskEndRunnable: Runnable?) {
        this.taskEndRunnable = taskEndRunnable
    }

    fun releaseTaskEndRunnable() {
        taskEndRunnable = null
    }

    fun setAction(action: NotificationCompat.Action?) {
        this.action = action
    }

    fun initNotification(filename: String) {
        manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "ascend:okdownload"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                OPERATION_INFO,
                NotificationManager.IMPORTANCE_MIN
            )
            manager!!.createNotificationChannel(channel)
        }
        builder = NotificationCompat.Builder(context, channelId)
        builder.setDefaults(Notification.DEFAULT_LIGHTS)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentTitle(OPERATION_INFO)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Downloading $filename")
            )
            .setSmallIcon(R.mipmap.ic_launcher)
        if (action != null) {
            builder.addAction(action)
        }
    }

    override fun taskStart(task: DownloadTask) {
        Log.d("NotificationActivity", "taskStart")
        builder.setTicker("taskStart")
        builder.setOngoing(true)
        builder.setAutoCancel(false)
        builder.setContentText("The task is started")
        builder.setProgress(0, 0, true)
        manager!!.notify(task.id, builder.build())
    }

    override fun connectStart(
        task: DownloadTask, blockIndex: Int,
        requestHeaderFields: Map<String, List<String>>
    ) {
        builder.setProgress(0, 0, true)
        manager!!.notify(task.id, builder.build())
    }

    override fun connectEnd(
        task: DownloadTask, blockIndex: Int, responseCode: Int,
        responseHeaderFields: Map<String, List<String>>
    ) {
        builder.setProgress(0, 0, true)
        manager!!.notify(task.id, builder.build())
    }

    override fun infoReady(
        task: DownloadTask,
        info: BreakpointInfo,
        fromBreakpoint: Boolean,
        model: Listener4SpeedModel
    ) {
        if (fromBreakpoint) {
            builder.setTicker("fromBreakpoint")
        } else {
            builder.setTicker("fromBeginning")
        }
        builder.setProgress(info.totalLength.toInt(), info.totalOffset.toInt(), true)
        manager!!.notify(task.id, builder.build())
        totalLength = info.totalLength.toInt()
    }

    override fun progressBlock(
        task: DownloadTask, blockIndex: Int,
        currentBlockOffset: Long,
        blockSpeed: SpeedCalculator
    ) {
    }

    override fun progress(
        task: DownloadTask, currentOffset: Long,
        taskSpeed: SpeedCalculator
    ) {
        builder.setContentText("Downloading : ${taskSpeed.speed()}")
        builder.setProgress(totalLength, currentOffset.toInt(), false)
        manager!!.notify(task.id, builder.build())
    }

    override fun blockEnd(
        task: DownloadTask,
        blockIndex: Int,
        info: BlockInfo,
        blockSpeed: SpeedCalculator
    ) {
    }

    override fun taskEnd(
        task: DownloadTask,
        cause: EndCause,
        realCause: Exception?,
        taskSpeed: SpeedCalculator
    ) {
        builder.setOngoing(false)
        builder.setAutoCancel(true)
        builder.setTicker("taskEnd $cause")
        if (cause == EndCause.COMPLETED) {
            builder.setProgress(1, 1, false)
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(SUCCESS)
            )
        } else {
            builder.setProgress(1, 1, false)
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(FAIL)
            )
        }
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (taskEndRunnable != null) taskEndRunnable!!.run()
                manager!!.notify(task.id, builder.build())
            }, // because of on some android phone too frequency notify for same id would be
            // ignored.
            100
        )
    }

    companion object {
        private const val OPERATION_INFO = "Downloading file"
        private const val SUCCESS = "Download successfully completed!"
        private const val FAIL = "Download failed!"
    }

}