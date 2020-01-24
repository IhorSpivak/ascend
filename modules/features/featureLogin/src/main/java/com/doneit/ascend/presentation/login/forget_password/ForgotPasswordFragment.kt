package com.doneit.ascend.presentation.login.forget_password

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.login.databinding.FragmentForgotPasswordBinding
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.getNotNullString
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    override val viewModelModule = ForgotPasswordViewModelModule.get(this)
    override val viewModel: ForgotPasswordContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        binding.toolbar.imBack.setOnClickListener {
            viewModel.onBackClick()
        }

        binding.phoneLayout.phoneCode.getSelectedCode().observe(this, Observer { code ->
            if (code != viewModel.phoneModel.phoneCode.getNotNullString()) {
                viewModel.phoneModel.phoneCode.set(code)
            }
        })

        binding.phoneLayout.phoneCode.touchListener = {
            hideKeyboard()
        }
    }

    override fun handleErrorMessage(message: PresentationMessage) {
        when(message.id) {
            Messages.DEFAULT_ERROR.getId() -> {
                showDefaultError(message.content!!)
            }
        }
    }
}
