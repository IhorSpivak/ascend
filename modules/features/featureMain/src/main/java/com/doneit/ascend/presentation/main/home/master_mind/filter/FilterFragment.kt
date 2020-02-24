package com.doneit.ascend.presentation.main.home.master_mind.filter

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentFilterBinding
import com.doneit.ascend.presentation.main.home.master_mind.MasterMindContract
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class FilterFragment : BaseFragment<FragmentFilterBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<FilterContract.ViewModel>() with provider {
            instance<MasterMindContract.ViewModel>()
        }
    }
    override val viewModel: FilterContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}