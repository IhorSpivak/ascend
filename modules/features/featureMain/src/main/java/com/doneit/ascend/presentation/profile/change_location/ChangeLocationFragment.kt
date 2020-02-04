package com.doneit.ascend.presentation.profile.change_location

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import androidx.core.view.GestureDetectorCompat
import androidx.core.widget.doOnTextChanged
import com.doneit.ascend.presentation.common.DefaultGestureDetectorListener
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChangeLocationBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.models.LocationModel
import com.doneit.ascend.presentation.profile.change_location.common.CountriesAdapter
import com.doneit.ascend.presentation.profile.common.ProfileViewModel
import com.doneit.ascend.presentation.utils.insert
import com.doneit.ascend.presentation.utils.fetchCountryListWithReflection
import com.doneit.ascend.presentation.utils.getCurrentCountyISO
import com.doneit.ascend.presentation.utils.toLocationModel
import com.rilixtech.widget.countrycodepicker.Country
import kotlinx.android.synthetic.main.fragment_change_location.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChangeLocationFragment :
    BaseFragment<FragmentChangeLocationBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChangeLocationContract.ViewModel>() with provider { vmShared<ProfileViewModel>(instance()) }
    }

    override val viewModel: ChangeLocationContract.ViewModel by instance()
    private val adapter by lazy {
        CountriesAdapter(
            fetchCountryListWithReflection(context!!).insert(
                0,
                Country("", "", "")
            )
        )
    }
    private val mDetector by lazy {
        GestureDetectorCompat(context, object : DefaultGestureDetectorListener() {
            override fun onSingleTapUp(p0: MotionEvent?): Boolean {
                return true
            }
        })
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        with(binding) {

            imBack.setOnClickListener {
                hideKeyboard()
                viewModel.goBack()
            }
        }

        initSpinner()
        binding.etCity.doOnTextChanged { text, start, count, after ->
            invalidateSaveStatus()
        }

        binding.btnSave.setOnClickListener {
            val city = etCity.text.toString()
            val country = countyPicker.selectedItem as Country
            viewModel.updateLocation(city, country.name)
            hideKeyboard()
            viewModel.goBack()
        }

        val location = arguments?.getString(CURRENT_USER_LOCATION)?.toLocationModel()
        if (location == null) {
            initDefaultSelection()
        } else {
            setupLocation(location)
        }
    }

    private fun initSpinner() {
        countyPicker.adapter = adapter
        countyPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                invalidateSaveStatus()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        countyPicker.setOnTouchListener { view, motionEvent ->
            if (mDetector.onTouchEvent(motionEvent)) {
                hideKeyboard()
            }
            false
        }
    }

    private fun setupLocation(location: LocationModel) {
        binding.etCity.setText(location.city)
        val index = adapter.countries.indexOfFirst { it.name == location.county }
        if (index != -1) {
            countyPicker.setSelection(index)
        }
    }

    private fun initDefaultSelection() {
        val currentCounty = context!!.getCurrentCountyISO()
        val index = adapter.countries.indexOfFirst { it.iso == currentCounty }
        if (index != -1) {
            countyPicker.setSelection(index)
        }
    }

    private fun invalidateSaveStatus() {
        var isEnabled = false

        if (countyPicker.selectedItemPosition > 0 //considering first junk item
            && etCity.text.isNotBlank()
        ) {
            isEnabled = true
        }

        btnSave.isEnabled = isEnabled
    }

    companion object {
        private const val CURRENT_USER_LOCATION = "CURRENT_USER_LOCATION"

        fun newInstance(initialLocation: String?): ChangeLocationFragment {
            val fragment = ChangeLocationFragment()
            fragment.arguments = Bundle().apply {
                putString(CURRENT_USER_LOCATION, initialLocation)
            }
            return fragment
        }
    }
}