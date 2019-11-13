package com.doneit.ascend.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment : Fragment {

    private val compositeDisposable = CompositeDisposable()

    constructor(): super()
    constructor(@LayoutRes layoutID: Int) : super(layoutID)

    fun disposeOnDestroy(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
