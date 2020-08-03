package com.doneit.ascend.presentation.main.base

import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.UnifiedListenerManager


object GlobalTaskManager {
    private val manager: UnifiedListenerManager = UnifiedListenerManager()
    fun addAutoRemoveListenersWhenTaskEnd(id: Int) {
        manager.addAutoRemoveListenersWhenTaskEnd(id)
    }

    fun attachListener(
        task: DownloadTask,
        listener: DownloadListener
    ) {
        manager.attachListener(task, listener)
    }

    fun enqueueTask(
        task: DownloadTask,
        listener: DownloadListener
    ) {
        manager.enqueueTaskWithUnifiedListener(task, listener)
    }
}