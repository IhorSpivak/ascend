package com.doneit.ascend.presentation.profile.regular_user.community

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCommunityBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.profile.regular_user.community.common.CommunityAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CommunityContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: CommunityContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvCommunityBox.layoutManager = GridLayoutManager(context!!, COLUMNS_COUNT)

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            binding.rvCommunityBox.adapter = CommunityAdapter(it)
        })
    }

    companion object {
        private const val COLUMNS_COUNT = 2
    }
}