package com.doneit.ascend.presentation.profile.notification_settings

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentNotificationSettingsBinding
import org.kodein.di.generic.instance

class NotificationSettingsFragment : BaseFragment<FragmentNotificationSettingsBinding>() {

    override val viewModelModule = NotificationSettingsViewModelModule.get(this)
    override val viewModel: NotificationSettingsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}