package com.doneit.ascend.presentation.main.create_group.create_webinar

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupArgs
import com.doneit.ascend.presentation.main.databinding.FragmentCreateWebinarBinding
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class CreateWebinarFragment : ArgumentedFragment<FragmentCreateWebinarBinding, CreateGroupArgs>(){
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<CreateWebinarContract.ViewModel>() with provider {
            instance<CreateWebinarContract.ViewModel>()
        }
    }

    override val viewModel: CreateWebinarContract.ViewModel by instance()
    override fun viewCreated(savedInstanceState: Bundle?) {

    }
}