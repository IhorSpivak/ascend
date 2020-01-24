package com.doneit.ascend.presentation.profile.regular_user.age

import android.os.Bundle
import com.doneit.ascend.domain.entity.MonthEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAgeBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.utils.extensions.toCalendar
import com.doneit.ascend.presentation.utils.extensions.toDayOfMonth
import com.doneit.ascend.presentation.utils.extensions.toMonth
import com.doneit.ascend.presentation.utils.extensions.toYear
import kotlinx.android.synthetic.main.fragment_age.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class AgeFragment : BaseFragment<FragmentAgeBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<AgeContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: AgeContract.ViewModel by instance()

    private val defaultBirthday by lazy {
        val calendar = Date().toCalendar()
        val year = calendar.get(Calendar.YEAR)
        calendar.set(Calendar.YEAR, year - DEFAULT_USER_AGE)

        calendar.time
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.monthPicker.data = MonthEntity.values().toList()

        binding.dayPicker.setOnItemSelectedListener { _, data, _ ->
            val day = data as Int

            updateSelection(d = day)
        }

        binding.monthPicker.setOnItemSelectedListener { _, data, _ ->
            val month = (data as MonthEntity)

            binding.dayPicker.month = month.toNumeric()
            updateSelection(m = month)
        }

        binding.yearPicker.setOnItemSelectedListener { _, data, _ ->
            val year = data as Int

            binding.dayPicker.year = year
            updateSelection(y = year)
        }

        viewModel.birthdaySelected.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val birthday = it ?: defaultBirthday

            val year = birthday.toYear()
            val month = birthday.toMonth()
            val day = birthday.toDayOfMonth()

            binding.age = UserEntity.getAge(birthday)
            binding.yearPicker.selectedYear = year
            binding.monthPicker.setSelectedItemPosition(month, false)
            binding.dayPicker.selectedDay = day
        })
    }

    private fun updateSelection(y: Int? = null, m: MonthEntity? = null, d: Int? = null) {
        val year = y ?: yearPicker.selectedYear
        val month = m ?: monthPicker.data[binding.monthPicker.selectedItemPosition] as MonthEntity
        val day = d ?: dayPicker.selectedDay

        viewModel.onBirthdaySelected(year, month, day)
    }

    companion object {
        private const val DEFAULT_USER_AGE = 18
    }
}