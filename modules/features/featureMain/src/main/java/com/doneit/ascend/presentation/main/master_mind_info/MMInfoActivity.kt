package com.doneit.ascend.presentation.main.master_mind_info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.entity.MasterMindEntity
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

class MMInfoActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("GroupInfoActivity") {
        bind<MMInfoRouter>() with singleton {
            MMInfoRouter(
                this@MMInfoActivity
            )
        }

        bind<MMInfoContract.Router>() with singleton { instance<MMInfoRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = MMInfoViewModel::class.java.simpleName) with provider {
            MMInfoViewModel(
                instance(),
                instance(),
                instance()
            )
        }
        bind<MMInfoContract.ViewModel>() with provider { vm<MMInfoViewModel>(instance()) }
    }

    fun getContainerId() = R.id.container
    private val viewModel: MMInfoContract.ViewModel by instance()
    private lateinit var binding: ActivityMasterMindProfileBindingImpl
    private var currentDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_master_mind_profile)
        binding.lifecycleOwner = this
        binding.model = viewModel

        val model = intent.getParcelableExtra<MasterMindEntity>(MM_ENTITY)
        viewModel.setModel(model)

        viewModel.sendReportStatus.observe(this) {
            if (it == true) {
                currentDialog?.dismiss()
            } else {
                Toast.makeText(this, "Send Error", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.showActionButtons.observe(this) {
            btnFollow.visibility = View.GONE
            btnUnFollow.visibility = View.GONE
        }

        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(this) {
                viewModel.report(it)
            }
            currentDialog?.show()
        }

        btnBack.setOnClickListener {
            viewModel.goBack()
        }

        rbRatingSet.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                viewModel.setRating(rating.toInt())
            }
        }

        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(
                this
            ) {
                viewModel.sendReport(it)
            }

            currentDialog?.show()
        }
    }

    companion object {
        const val MM_ENTITY = "MM_ENTITY"
    }
}
