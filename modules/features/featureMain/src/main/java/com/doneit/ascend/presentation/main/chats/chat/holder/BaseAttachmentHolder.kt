package com.doneit.ascend.presentation.main.chats.chat.holder

import android.os.Environment
import android.view.View
import com.doneit.ascend.presentation.main.base.GlobalTaskManager
import com.doneit.ascend.presentation.main.chats.chat.common.NotificationSampleListener
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import java.io.File

abstract class BaseAttachmentHolder(
    itemView: View
) : BaseMessageHolder(itemView) {

    fun isFileExist(filename: String): Boolean {
        val downloadsFolder = itemView.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(downloadsFolder, filename)
        return outputFile.exists()
    }

    fun downloadFile(url: String, filename: String) {
        val downloadsFolder = itemView.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val outputFile = File(downloadsFolder, filename)
        if (!outputFile.exists()) {
            outputFile.createNewFile()
        }
        DownloadTask.Builder(url, outputFile)
            .setFilename(filename)
            .setPassIfAlreadyCompleted(false)
            .setMinIntervalMillisCallbackProcess(80)
            .setAutoCallbackToUIThread(false)
            .build().also {
                GlobalTaskManager.enqueueTask(it, initListener(filename))
            }
    }

    private fun initListener(filename: String): DownloadListener {
        return NotificationSampleListener(itemView.context).apply {
            attachTaskEndRunnable(Runnable {
                resourceDownloaded()
            })
            initNotification(filename)
        }
    }

    protected abstract fun resourceDownloaded()
}