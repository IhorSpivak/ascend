package com.doneit.ascend.presentation.main.master_mind_info.mm_content.livestreams

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentFilterBinding
import com.doneit.ascend.presentation.main.databinding.FragmentFilterMmLivestreamsBinding
import com.doneit.ascend.presentation.main.home.master_mind.MasterMindContract
import com.doneit.ascend.presentation.main.home.master_mind.filter.FilterContract
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class FilterMMLivestreamFragment : BaseFragment<FragmentFilterMmLivestreamsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<FilterContract.ViewModel>() with provider {
            instance<MasterMindContract.ViewModel>()
        }
    }
    override val viewModel: FilterContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {

    }
}