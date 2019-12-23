package com.doneit.ascend.presentation.main.notification

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.databinding.ActivityNotificationBinding
import com.doneit.ascend.presentation.main.notification.common.NotificationsAdapter
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NotificationActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("NotificationActivity") {
        bind<NotificationRouter>() with singleton {
            NotificationRouter(
                this@NotificationActivity
            )
        }

        bind<NotificationContract.Router>() with singleton { instance<NotificationRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = NotificationViewModel::class.java.simpleName) with provider {
            NotificationViewModel(
                instance(),
                instance()
            )
        }
        bind<NotificationContract.ViewModel>() with provider { vm<NotificationViewModel>(instance()) }
    }

    private val adapter: NotificationsAdapter by lazy {
        NotificationsAdapter(
            {
                viewModel.onNotificationClick(it)
            },
            {
                viewModel.onDelete(it)
            }
        )
    }

    private val viewModel: NotificationContract.ViewModel by instance()
    private lateinit var binding: ActivityNotificationBinding

    fun getContainerId() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)

        binding.rvNotifications.adapter = this.adapter

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.notifications.observe(this, Observer {
            this.adapter.submitList(it)
        })
    }
}