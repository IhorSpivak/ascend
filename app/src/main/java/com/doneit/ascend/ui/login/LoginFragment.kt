package com.doneit.ascend.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.doneit.ascend.R
import com.doneit.ascend.databinding.LoginFragmentBinding
import com.doneit.ascend.ui.BaseFragment

class LoginFragment: BaseFragment() {

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LoginFragmentBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            lifecycleOwner = this@LoginFragment
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.model = viewModel

        viewModel.loginEvent.observe(this, Observer { success ->
            if (success) {
//                findNavController().navigate(R.id.signUpFragment)
            }
        })

        binding.signUpView.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }

        binding.forgotView.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        binding.policyView.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        binding.termsView.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
    }
}

