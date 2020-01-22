package com.doneit.ascend.presentation.profile.regular_user.age

import android.os.Bundle
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAgeBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class AgeFragment : BaseFragment<FragmentAgeBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<AgeContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: AgeContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.monthPicker.data = MonthEntity.values().toList()

        binding.monthPicker.setOnItemSelectedListener { _, data, position ->
            val month = (data as MonthEntity)

            binding.dayPicker.month = month.toNumeric()
        }

        binding.yearPicker.setOnItemSelectedListener { _, data, position ->
            binding.dayPicker.year = data as Int
        }
    }
}