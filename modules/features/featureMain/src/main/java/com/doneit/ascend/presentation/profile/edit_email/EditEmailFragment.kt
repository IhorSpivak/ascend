package com.doneit.ascend.presentation.profile.edit_email

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChangeEmailBinding
import org.kodein.di.generic.instance

class EditEmailFragment : BaseFragment<FragmentChangeEmailBinding>() {

    override val viewModelModule = EditEmailViewModelModule.get(this)
    override val viewModel: EditEmailContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}