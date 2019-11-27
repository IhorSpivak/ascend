package com.doneit.ascend.presentation.login.first_time_login

import android.os.Bundle
import android.view.View
import com.doneit.ascend.presentation.login.databinding.FragmentFirstTimeLoginBinding
import com.doneit.ascend.presentation.login.first_time_login.common.FirstTimeLoginArgs
import com.doneit.ascend.presentation.login.first_time_login.common.QuestionsAdapter
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import kotlinx.android.synthetic.main.fragment_first_time_login.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.kodein.di.generic.instance

class FirstTimeLoginFragment :
    ArgumentedFragment<FragmentFirstTimeLoginBinding, FirstTimeLoginArgs>() {

    override val viewModelModule = FirstTimeLoginViewModelModule.get(this)
    override val viewModel: FirstTimeLoginContract.ViewModel by instance()

    private val adapter: QuestionsAdapter by lazy {
        QuestionsAdapter(mutableListOf(), viewModel)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.adapter = adapter
        binding.executePendingBindings()

        toolbar.imBack.visibility = View.INVISIBLE
    }
}