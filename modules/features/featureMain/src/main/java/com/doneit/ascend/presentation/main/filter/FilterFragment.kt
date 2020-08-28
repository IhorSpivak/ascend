package com.doneit.ascend.presentation.main.filter

import android.app.Dialog
import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseBottomSheetFragment
import com.doneit.ascend.presentation.main.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

open class FilterFragment : BaseBottomSheetFragment<FragmentFilterBinding>() {

    override val viewModelModule: Kodein.Module
        get() = FilterViewModelModule.get(this)

    override val viewModel: FilterContract.ViewModel<FilterModel> by instance()

    @Suppress("unchecked_cast")
    protected open val filterListener: FilterListener<FilterModel> by lazy {
        require(requireParentFragment() is FilterListener<*>) {
            "Parent fragment should implement FilterListener"
        }
        requireParentFragment() as FilterListener<FilterModel>
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
            requireNotNull(requireArguments().getParcelable<FilterModel>(KEY_INIT_FILTER))
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

    private fun initFilter(initFilter: FilterModel) = with(binding.daysOfWeek) {
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
        fromPicker.setOnItemSelectedListener { _, data, position ->
            (data as String)
        }
    }

    private fun setClickListeners() = with(binding) {
        btnApply.setOnClickListener {
            filterListener.updateFilter(viewModel.filter)
            dismiss()
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        private const val KEY_INIT_FILTER = "key_init_filter"

        fun newInstance(model: FilterModel) = FilterFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_INIT_FILTER, model)
            }
        }
    }
}