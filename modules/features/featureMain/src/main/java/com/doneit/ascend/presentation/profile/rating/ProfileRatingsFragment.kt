package com.doneit.ascend.presentation.profile.rating

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentProfileRatingsBinding
import com.doneit.ascend.presentation.profile.rating.common.RatingAdapter
import org.kodein.di.generic.instance

class ProfileRatingsFragment : BaseFragment<FragmentProfileRatingsBinding>() {

    override val viewModelModule = ProfileRatingsViewModelModule.get(this)
    override val viewModel: ProfileRatingsContract.ViewModel by instance()

    private val adapter: RatingAdapter by lazy { RatingAdapter() }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvRatings.adapter = adapter

        viewModel.ratings.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}