package com.doneit.ascend.presentation.main.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DKodein
import org.kodein.di.generic.instance

class CommonViewModelFactory(
    private val provider: DKodein
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return provider.instance<ViewModel>(tag = modelClass.simpleName) as? T?:modelClass.newInstance()
    }
}