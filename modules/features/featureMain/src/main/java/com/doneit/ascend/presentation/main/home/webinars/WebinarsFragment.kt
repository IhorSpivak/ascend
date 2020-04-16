package com.doneit.ascend.presentation.main.home.webinars

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarsBinding
import org.kodein.di.generic.instance

class WebinarsFragment : BaseFragment<FragmentWebinarsBinding>(){
    override val viewModelModule = WebinarsViewModelModule.get(this)
    override val viewModel: WebinarsContract.ViewModel by instance()
    override fun viewCreated(savedInstanceState: Bundle?) {
    }
}