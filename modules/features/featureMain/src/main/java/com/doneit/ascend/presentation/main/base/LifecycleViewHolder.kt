package com.doneit.ascend.presentation.main.base

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

open class LifecycleViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun unbind() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    fun onAppear(errors: LiveData<Int?>) {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

}