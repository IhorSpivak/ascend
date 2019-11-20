package com.doneit.ascend.presentation.login.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.utils.fetchCountryListWithReflection
import com.doneit.ascend.presentation.login.views.common.CountriesAdapter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.android.synthetic.main.view_phone_code.view.*
import kotlin.math.max

class PhoneCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val selectedCode = MutableLiveData<String>()
    private lateinit var countriesAdapter: CountriesAdapter

    init {
        View.inflate(context, R.layout.view_phone_code, this)
    }

    fun getSelectedCode(): LiveData<String> {
        return selectedCode
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initCountryCodesDropDown()
        fetchCurrentCountryCode()
    }

    override fun getBaseline(): Int {
        return picker.baseline
    }

    private fun initCountryCodesDropDown() {
        val data = fetchCountryListWithReflection(context!!)
        countriesAdapter = CountriesAdapter(data)
        picker.adapter = countriesAdapter
        picker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val phoneCode = countriesAdapter.countries[p2].phoneCode

                selectedCode.postValue(String.format(CODE_FORMAT, phoneCode))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        selectByPhoneCode(defaultCountyCode)
    }

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
        private const val CODE_FORMAT = "+%s"
    }

}