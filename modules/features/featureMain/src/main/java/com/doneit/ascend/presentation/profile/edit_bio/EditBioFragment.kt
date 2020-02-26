package com.doneit.ascend.presentation.profile.edit_bio

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentEditBioBinding
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.utils.extensions.focusRequest
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import kotlinx.android.synthetic.main.view_multiline_edit_with_error.view.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class EditBioFragment : BaseFragment<FragmentEditBioBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<EditBioContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }
    
    override val viewModel: EditBioContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        with(binding) {

            btnBack.setOnClickListener {
                hideKeyboard()
                viewModel.goBack()
            }

            btnSave.setOnClickListener {
                val newValue = etBio.text
                viewModel.updateBio(newValue)
                hideKeyboard()
                viewModel.goBack()
            }
            etBio.multilineEditText.focusRequest()
        }
    }
}