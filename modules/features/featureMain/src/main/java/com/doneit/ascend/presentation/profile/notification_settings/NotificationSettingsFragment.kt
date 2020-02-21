package com.doneit.ascend.presentation.profile.notification_settings

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentNotificationSettingsBinding
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.utils.extensions.vmShared
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class NotificationSettingsFragment : BaseFragment<FragmentNotificationSettingsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<NotificationSettingsContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }
    override val viewModel: NotificationSettingsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.meetingStarted.switcher.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.onChanged(NotificationSettingsItem.MEETING_STARTED, b)
        }

        binding.newGroups.switcher.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.onChanged(NotificationSettingsItem.NEW_GROUPS, b)
        }

        binding.inviteMeetings.switcher.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.onChanged(NotificationSettingsItem.INVITE_TO_MEETING, b)
        }
    }
}