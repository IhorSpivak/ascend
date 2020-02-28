package com.doneit.ascend.presentation.main.create_group.master_mind.individual

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentCreateIndividualGroupBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class IndividualGroupFragment : BaseFragment<FragmentCreateIndividualGroupBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<IndividualGroupContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: IndividualGroupContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}