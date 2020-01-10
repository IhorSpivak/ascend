package com.doneit.ascend.presentation.profile.edit_phone

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.FragmentEditPhoneBinding
import com.doneit.ascend.presentation.main.extensions.hideKeyboard
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.extensions.waitForLayout
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class EditPhoneFragment : BaseFragment<FragmentEditPhoneBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag=EditPhoneViewModel::class.java.simpleName) with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = EditPhoneViewModel::class.java.simpleName) with provider {
            EditPhoneViewModel(
                instance(),
                instance()
            )
        }
        bind<EditPhoneContract.ViewModel>() with provider { vmShared<EditPhoneViewModel>(instance(tag=EditPhoneViewModel::class.java.simpleName)) }
    }
    override val viewModel: EditPhoneContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.phoneLayout.phoneCode.touchListener = {
            hideKeyboard()
        }

        binding.root.waitForLayout {
            viewModel.init() //in order to wait for phoneCode layout initialization
        }
    }
}