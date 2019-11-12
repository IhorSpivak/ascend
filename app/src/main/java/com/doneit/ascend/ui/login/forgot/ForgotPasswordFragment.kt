package com.doneit.ascend.ui.login.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.doneit.ascend.databinding.ForgotFragmentBinding
import com.doneit.ascend.ui.BaseFragment

class ForgotPasswordFragment : BaseFragment() {

    private lateinit var binding: ForgotFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ForgotFragmentBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            lifecycleOwner = this@ForgotPasswordFragment
        }
        .root
}
