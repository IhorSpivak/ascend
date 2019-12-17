package com.doneit.ascend.presentation.main.group_info

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityGroupInfoBindingImpl
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class GroupInfoActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("GroupInfoActivity") {
        bind<GroupInfoRouter>() with singleton {
            GroupInfoRouter(
                this@GroupInfoActivity
            )
        }

        bind<GroupInfoContract.Router>() with singleton { instance<GroupInfoRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = GroupInfoViewModel::class.java.simpleName) with provider {
            GroupInfoViewModel(
                instance(),
                instance()
            )
        }
        bind<GroupInfoContract.ViewModel>() with provider { vm<GroupInfoViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container

    private val viewModel: GroupInfoContract.ViewModel by instance()
    private lateinit var binding: ActivityGroupInfoBindingImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info)
        binding.lifecycleOwner = this
        binding.model = viewModel

        val groupId = intent.getLongExtra(GROUP_ID, -1)
        viewModel.loadData(groupId)


    }

    companion object {
        const val GROUP_ID = "GROUP_ID"
    }
}