package com.doneit.ascend.presentation.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.login.common.CountriesAdapter
import com.doneit.ascend.presentation.login.databinding.ActivityLoginBinding
import com.doneit.ascend.presentation.login.utils.fetchCountryListWithReflection
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vrgsoft.core.presentation.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import kotlin.math.max


class LogInActivity: BaseActivity() {
    override fun diModule() = Kodein.Module("LogInActivity") {
        bind<LogInContract.ViewModel>() with provider {
            LogInViewModel(
                instance(),
                instance()
            )
        }
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LogInContract.ViewModel by instance()
    private lateinit var countriesAdapter: CountriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.model = viewModel

        initCountyCodes()
        initWithPermissions()
    }

    private fun initCountyCodes() {
        val data = fetchCountryListWithReflection(applicationContext)
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
        EzPermission.with(this)
            .permissions(
                Manifest.permission.READ_PHONE_STATE
            )
            .request { granted, denied, permanentlyDenied ->
                for(permission in granted) {
                    if(permission == Manifest.permission.READ_PHONE_STATE) {
                        val telephonyManager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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