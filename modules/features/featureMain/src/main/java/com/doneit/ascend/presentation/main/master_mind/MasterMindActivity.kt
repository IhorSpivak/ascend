package com.doneit.ascend.presentation.main.master_mind

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityMasterMindBinding
import com.doneit.ascend.presentation.main.master_mind.common.TabAdapter
import com.doneit.ascend.presentation.main.master_mind.list.ListContract
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MasterMindActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("MasterMindActivity") {
        bind<MasterMindRouter>() with singleton {
            MasterMindRouter(
                this@MasterMindActivity
            )
        }

        bind<MasterMindContract.Router>() with singleton { instance<MasterMindRouter>() }

        bind<ListContract.Router>() with singleton { instance<MasterMindRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = MasterMindViewModel::class.java.simpleName) with provider {
            MasterMindViewModel(
                instance()
            )
        }
        bind<MasterMindContract.ViewModel>() with provider { vm<MasterMindViewModel>(instance()) }
    }

    private val viewModel: MasterMindContract.ViewModel by instance()
    private lateinit var binding: ActivityMasterMindBinding

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter.newInstance(this, supportFragmentManager)
    }

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_master_mind)

        binding.tabAdapter = this.tabAdapter

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }
    }
}