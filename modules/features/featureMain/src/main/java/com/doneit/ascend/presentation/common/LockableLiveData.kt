package com.doneit.ascend.presentation.common

import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean

class LockableLiveData<T>(
    initValue: T
) : MutableLiveData<T>(initValue) {
    private val isBlocked = AtomicBoolean(false)

    //Block value changing
    fun lock() {
        isBlocked.set(true)
    }

    //Unblock value changing
    fun unlock() {
        isBlocked.set(false)
    }
    
    override fun setValue(value: T) {
        if (!isBlocked.get()) {
            super.setValue(value)
        }
    }
}