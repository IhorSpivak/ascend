package com.doneit.ascend.presentation.main.filter

import android.app.Dialog
import android.os.Bundle
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseBottomSheetFragment
import com.doneit.ascend.presentation.main.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

abstract class FilterFragment<T : FilterModel> : BaseBottomSheetFragment<FragmentFilterBinding>() {

    protected abstract val viewModel: FilterContract.ViewModel<T>

    @Suppress("unchecked_cast")
    protected open val filterListener: FilterListener<FilterModel> by lazy {
        require(requireParentFragment() is FilterListener<*>) {
            "Parent fragment should implement FilterListener"
        }
        requireParentFragment() as FilterListener<FilterModel>
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_filter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val d = this as BottomSheetDialog
                BottomSheetBehavior.from(
                    d.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                ).setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        val initFilter =
            requireNotNull(requireArguments().getParcelable<T>(KEY_INIT_FILTER))
        setupBinding()
        initFilter(initFilter)
    }

    protected open fun setupBinding() = with(binding) {
        model = viewModel
        fromPicker.data = viewModel.dataSource
        toPicker.data = viewModel.dataSource
        configureDayButtons()
        configureStartEndDate()
        setClickListeners()
    }

    protected open fun initFilter(initFilter: T) = with(binding.daysOfWeek) {
        viewModel.setFilter(initFilter)
        initFilter.selectedDays.forEach {
            when (it) {
                DayOfWeek.SUNDAY -> btnSu.isChecked = true
                DayOfWeek.MONDAY -> btnMo.isChecked = true
                DayOfWeek.TUESDAY -> btnTu.isChecked = true
                DayOfWeek.WEDNESDAY -> btnWe.isChecked = true
                DayOfWeek.THURSDAY -> btnTh.isChecked = true
                DayOfWeek.FRIDAY -> btnFr.isChecked = true
                DayOfWeek.SATURDAY -> btnSa.isChecked = true
            }
        }
        binding.fromPicker.setSelectedItemPosition(initFilter.timeFrom.toInt() / 5, false)
        binding.toPicker.setSelectedItemPosition(initFilter.timeTo.toInt() / 5, false)
    }

    private fun configureDayButtons() = with(binding.daysOfWeek) {
        btnSu.setOnClickListener { viewModel.selectDay(DayOfWeek.SUNDAY) }
        btnMo.setOnClickListener { viewModel.selectDay(DayOfWeek.MONDAY) }
        btnTu.setOnClickListener { viewModel.selectDay(DayOfWeek.TUESDAY) }
        btnWe.setOnClickListener { viewModel.selectDay(DayOfWeek.WEDNESDAY) }
        btnTh.setOnClickListener { viewModel.selectDay(DayOfWeek.THURSDAY) }
        btnFr.setOnClickListener { viewModel.selectDay(DayOfWeek.FRIDAY) }
        btnSa.setOnClickListener { viewModel.selectDay(DayOfWeek.SATURDAY) }
    }

    private fun configureStartEndDate() = with(binding) {
        fromPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.selectStartDate(getMinutesOfDay(data as String))
        }
        toPicker.setOnItemSelectedListener { _, data, _ ->
            viewModel.selectEndDate(getMinutesOfDay(data as String))
        }
    }

    protected open fun setClickListeners() = with(binding) {
        btnApply.setOnClickListener {
            filterListener.updateFilter(viewModel.filter)
            dismiss()
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun getMinutesOfDay(data: String): Long {
        val timeFormat = SimpleDateFormat("K:mm aa", Locale.getDefault())
        val date = requireNotNull(timeFormat.parse(data))
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        return calendar.get(Calendar.HOUR_OF_DAY).toLong() * 60L + calendar.get(Calendar.MINUTE)
    }

    companion object {
        const val KEY_INIT_FILTER = "key_init_filter"
    }
}