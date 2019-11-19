package com.doneit.ascend.presentation.login.verify_phone

import android.os.Bundle
import com.doneit.ascend.presentation.login.databinding.FragmentVerifyPhoneBinding
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.vrgsoft.core.presentation.fragment.BaseFragment
import org.kodein.di.generic.instance

class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>() {

    override val viewModelModule = VerifyPhoneViewModelModule.get(this)
    override val viewModel: VerifyPhoneContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.setModel(arguments!![SIGN_UP_MODEL_TAG] as PresentationSignUpModel)//todo replace by shared ViewModel
    }

    companion object {
        private const val SIGN_UP_MODEL_TAG = "SIGN_UP_MODEL"

        fun getInstance(model: PresentationSignUpModel): VerifyPhoneFragment {
            return VerifyPhoneFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SIGN_UP_MODEL_TAG, model)
                }
            }
        }
    }
}