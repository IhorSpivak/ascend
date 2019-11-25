package com.doneit.ascend.presentation.login.new_password

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.databinding.FragmentNewPasswordBinding
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.utils.ConnectionObserver
import com.doneit.ascend.presentation.utils.showInfoDialog
import com.doneit.ascend.presentation.utils.showNoConnectionDialog
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedFragment
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NewPasswordFragment : ArgumentedFragment<FragmentNewPasswordBinding, NewPasswordArgs>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = NewPasswordViewModel::class.java.simpleName) with provider {
            NewPasswordViewModel(
                instance(),
                instance()
            )
        }
        bind<NewPasswordContract.ViewModel>() with singleton {
            vmShared<NewPasswordViewModel>(
                instance()
            )
        }
    }

    override val viewModel: NewPasswordContract.ViewModel by instance()
    private val connectionObserver: ConnectionObserver by lazy {
        ConnectionObserver(this@NewPasswordFragment.context!!)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.executePendingBindings()
        viewModel.removeErrors()

        viewModel.showSuccessSendSMSMessage.observe(this) {
            it?.let { resultIt ->
                if (resultIt) {
                    this.showInfoDialog(
                        getString(R.string.send_sms_title),
                        getString(R.string.send_sms_description)
                    )
                }
            }
        }

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }
    }

    override fun onResume() {
        super.onResume()

        connectionObserver.networkStateChanged.observe(this, Observer {
            if (!it) {
                this.showNoConnectionDialog(getString(R.string.connecting))
            }
        })
    }
}