package com.doneit.ascend.presentation.main.create_group

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHostCreateGroupBinding
import com.doneit.ascend.presentation.utils.GroupAction
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class CreateGroupHostFragment : BaseFragment<FragmentHostCreateGroupBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag=CreateGroupHostFragment::class.java.simpleName) with singleton { CommonViewModelFactory(kodein.direct) }

        bind<LocalRouter>() with provider {
            LocalRouter(
                this@CreateGroupHostFragment
            )
        }

        bind<ViewModel>(tag = CreateGroupViewModel::class.java.simpleName) with provider {
            CreateGroupViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<CreateGroupHostContract.ViewModel>() with provider {
            vm<CreateGroupViewModel>(
                instance(tag=CreateGroupHostFragment::class.java.simpleName)
            )
        }
    }

    override val viewModel: CreateGroupHostContract.ViewModel by instance()

    private var group: GroupEntity? = null
    private var what: String? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        GroupAction.values().forEach {
            if (requireArguments().containsKey(it.toString())){
                group = requireArguments().getParcelable(it.toString())
                if (group != null){
                    what = it.toString()
                }
            }
        }
        val args = requireArguments().getParcelable<CreateGroupArgs>(ArgumentedFragment.KEY_ARGS)!!
        viewModel.handleBaseNavigation(args, group, what)
    }

    fun getContainerId() = R.id.container
}