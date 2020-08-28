package com.doneit.ascend.presentation.main.filter

import androidx.lifecycle.ViewModelProvider
import com.vrgsoft.core.presentation.fragment.BaseFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object FilterViewModelModule {
    fun get(fragment: FilterFragment) = Kodein.Module("Filter"){
    bind<ViewModelProvider.Factory>(tag = "Filter") with singleton { object :BaseFactory<FilterViewModel>() {
        override fun createViewModel(): FilterViewModel {
            return FilterViewModel()
        }
    } }
    bind<FilterContract.ViewModel<FilterModel>>() with provider { fragment.vm<FilterViewModel>(instance(tag =
            "Filter")) }
    }
}