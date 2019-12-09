package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.create_group.common.ParticipantAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentCreateGroupBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.view_edit_with_error.view.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CreateGroupFragment : ArgumentedFragment<FragmentCreateGroupBinding, CreateGroupArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = CreateGroupViewModel::class.java.simpleName) with provider {
            CreateGroupViewModel(
                instance(),
                instance()
            )
        }
        bind<CreateGroupContract.ViewModel>() with provider {
            vmShared<CreateGroupViewModel>(
                instance()
            )
        }
    }

    override val viewModel: CreateGroupContract.ViewModel by instance()

    private val adapter: ParticipantAdapter by lazy {
        ParticipantAdapter(mutableListOf(), viewModel)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter

        tvTitle.text = getString(R.string.create_group)

        chooseSchedule.editText.setOnTouchListener { _, _ ->
            viewModel.chooseScheduleTouch()
            true
        }
    }
}