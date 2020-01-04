package com.doneit.ascend.presentation.profile.change_location

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChangeLocationBinding
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChangeLocationFragment : BaseFragment<FragmentChangeLocationBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChangeLocationContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: ChangeLocationContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        with(binding) {

            imBack.setOnClickListener {
                hideKeyboard()
                viewModel.goBack()
            }

            /*btnSave.setOnClickListener {
                val newValue = etBio.text
                viewModel.updateBio(newValue)
                hideKeyboard()
                viewModel.goBack()
            }
            etBio.focusRequest()*/
        }
    }
}