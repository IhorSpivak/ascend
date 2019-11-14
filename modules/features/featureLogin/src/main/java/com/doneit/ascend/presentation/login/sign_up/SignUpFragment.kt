package com.doneit.ascend.presentation.login.sign_up

import android.os.Bundle
import com.doneit.ascend.presentation.login.databinding.SignUpFragmentBinding
import com.vrgsoft.core.presentation.fragment.BaseFragment
import com.vrgsoft.core.presentation.fragment.BaseViewModel

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    override val viewModel: BaseViewModel
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun viewCreated(savedInstanceState: Bundle?) {
        /*val model =  ViewModelProvider(this).get(SignUpViewModel::class.java)
        binder.model = model

        model.registrationEvent.observe(this, Observer { success ->
            findNavController().navigate(R.id.loginFragment)
        })*/
    }
}
