package com.doneit.ascend.presentation.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RvLazyAdapter<out T : RecyclerView.Adapter<*>>(
    lock: Any? = null,
    initializer: () -> Pair<T, () -> RecyclerView>
) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private var _value: T? = null
    private var recyclerProvider: (() -> RecyclerView)? = null
    private val lock = lock ?: this
    private var initializer: (() -> Pair<T, () -> RecyclerView>)? = initializer

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        thisRef.viewLifecycleOwner.lifecycle.removeObserver(this)
        thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
        return synchronized(lock) {
            val _v2 = _value
            if (_v2 !== null) {
                _v2
            } else {
                init()
            }
        }
    }

    private fun init(): T {
        val typedValue = initializer!!()
        _value = typedValue.first
        recyclerProvider = typedValue.second
        initializer = null
        return typedValue.first
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun doOnCreate() {
        recyclerProvider?.invoke()?.adapter = _value
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun doOnDestroy() {
        recyclerProvider?.invoke()?.adapter = null
    }
}