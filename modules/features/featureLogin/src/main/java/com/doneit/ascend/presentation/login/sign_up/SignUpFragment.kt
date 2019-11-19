package com.doneit.ascend.presentation.login.sign_up

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.common.CountriesAdapter
import com.doneit.ascend.presentation.login.databinding.SignUpFragmentBinding
import com.doneit.ascend.presentation.login.utils.fetchCountryListWithReflection
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.vrgsoft.core.presentation.fragment.BaseFragment
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import org.kodein.di.generic.instance
import kotlin.math.max

class SignUpFragment : BaseFragment<SignUpFragmentBinding>() {

    override val viewModelModule = SignUpViewModelModule.get(this)
    override val viewModel: SignUpContract.ViewModel by instance()
    private lateinit var countriesAdapter: CountriesAdapter

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        imBack.setOnClickListener {
            viewModel.onBackClick()
        }

        initSignInSpannable()
        initCountryCodesDropDown()
        fetchCurrentCountryCode()
    }

    private fun initSignInSpannable() {
        val spannable = SpannableString(getString(R.string.already_member))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                viewModel.onBackClick()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(clickableSpan, 18, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val color = ContextCompat.getColor(context!!, R.color.defaultTextColor)
        spannable.setSpan(
            ForegroundColorSpan(color),
            18,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        signIn.text = spannable
        signIn.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initCountryCodesDropDown() {
        val data = fetchCountryListWithReflection(context!!)
        countriesAdapter = CountriesAdapter(data)
        picker.adapter = countriesAdapter
        picker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val phoneCode = countriesAdapter.countries[p2].phoneCode
                viewModel.registrationModel.phoneCode =
                    resources.getString(R.string.phone_format, phoneCode)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        selectByPhoneCode(defaultCountyCode)
    }

    //todo move to custom view
    @SuppressLint("MissingPermission")
    private fun fetchCurrentCountryCode() {
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.READ_PHONE_STATE
            )
            .request { granted, denied, permanentlyDenied ->
                for (permission in granted) {
                    if (permission == Manifest.permission.READ_PHONE_STATE) {
                        val telephonyManager =
                            context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        val regionCode = telephonyManager.networkCountryIso
                        val localCountryCode = PhoneNumberUtil.getInstance()
                            .parse(telephonyManager.line1Number, regionCode).countryCode
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
