package com.doneit.ascend.presentation.main.notification

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentNotificationBinding
import com.doneit.ascend.presentation.main.notification.common.NotificationsAdapter
import org.kodein.di.generic.instance

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    override val viewModelModule = NotificationViewModelModule.get(this)
    override val viewModel: NotificationContract.ViewModel by instance()

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

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.rvNotifications.adapter = this.adapter

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.notifications.observe(this, Observer {
            this.adapter.submitList(it)
        })
    }
}