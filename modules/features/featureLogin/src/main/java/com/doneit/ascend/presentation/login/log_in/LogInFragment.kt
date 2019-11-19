package com.doneit.ascend.presentation.login.log_in

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.AdapterView
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.common.CountriesAdapter
import com.doneit.ascend.presentation.login.databinding.FragmentLoginBinding
import com.doneit.ascend.presentation.login.utils.fetchCountryListWithReflection
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.generic.instance
import kotlin.math.max

class LogInFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModelModule = LogInViewModelModule.get(this)
    override val viewModel: LogInContract.ViewModel by instance()
    private lateinit var countriesAdapter: CountriesAdapter


    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        init()
        initWithPermissions()
    }
    private fun init() {
        val data = fetchCountryListWithReflection(context!!)
        countriesAdapter = CountriesAdapter(data)
        picker.adapter = countriesAdapter
        picker.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val phoneCode = countriesAdapter.countries[p2].phoneCode
                viewModel.loginModel.phoneCode = resources.getString(R.string.phone_format, phoneCode)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        selectByPhoneCode(defaultCountyCode)
    }

    @SuppressLint("MissingPermission")
    private fun initWithPermissions() {
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_PHONE_STATE
            )
            .request { granted, denied, permanentlyDenied ->
                for(permission in granted) {
                    if(permission == Manifest.permission.READ_PHONE_STATE) {
                        val telephonyManager = context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        val regionCode = telephonyManager.networkCountryIso
                        val localCountryCode = PhoneNumberUtil.getInstance().parse(telephonyManager.line1Number, regionCode).countryCode
                        selectByPhoneCode(localCountryCode.toString())
                    }
                }
            }
    }

    private fun selectByPhoneCode(code: String) {
        var position = countriesAdapter.getPositionByPhoneCode(code)
        position = max(position, 0)
        picker.setSelection(position)
    }

    companion object {
        private const val defaultCountyCode = "1"
    }
}