package com.doneit.ascend.presentation.main.home.community_feed.create_post

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCreatePostBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {
    override val viewModel: CreatePostContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = CreatePostViewModelModule.get(this)

    override fun viewCreated(savedInstanceState: Bundle?) {
    }
}