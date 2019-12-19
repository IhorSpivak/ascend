package com.doneit.ascend.presentation.main.master_mind_profile

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityMasterMindProfileBindingImpl
import kotlinx.android.synthetic.main.activity_master_mind_profile.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MMProfileActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("GroupInfoActivity") {
        bind<MMProfileRouter>() with singleton {
            MMProfileRouter(
                this@MMProfileActivity
            )
        }

        bind<MMProfileContract.Router>() with singleton { instance<MMProfileRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = MMProfileViewModel::class.java.simpleName) with provider {
            MMProfileViewModel(
                instance(),
                instance(),
                instance()
            )
        }
        bind<MMProfileContract.ViewModel>() with provider { vm<MMProfileViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container
    private val viewModel: MMProfileContract.ViewModel by instance()
    private lateinit var binding: ActivityMasterMindProfileBindingImpl
    private var currentDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_master_mind_profile)
        binding.lifecycleOwner = this
        binding.model = viewModel


        val id = intent.getLongExtra(MM_ID, -1)
        viewModel.loadData(id)

        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(this) {
                viewModel.report(it)
            }
            currentDialog?.show()
        }

        btnBack.setOnClickListener {
            viewModel.goBack()
        }
    }

    companion object {
        const val MM_ID = "MM_ID"
    }
}
