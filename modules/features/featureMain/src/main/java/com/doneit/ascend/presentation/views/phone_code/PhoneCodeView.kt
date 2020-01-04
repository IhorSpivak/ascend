package com.doneit.ascend.presentation.views.phone_code

import android.content.Context
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.views.phone_code.common.CountriesAdapter
import com.doneit.ascend.presentation.utils.fetchCountryListWithReflection
import com.doneit.ascend.presentation.utils.toNumericCode
import com.rilixtech.widget.countrycodepicker.Country
import kotlinx.android.synthetic.main.view_phone_code.view.*
import kotlin.math.max
@InverseBindingMethods(
    value = [
        InverseBindingMethod(
            type = PhoneCodeView::class,
            attribute = "code",
            method = "getCode"
        )
    ]
)
@BindingMethods(
    value = [
        BindingMethod(
            type = PhoneCodeView::class,
            attribute = "code",
            method = "setCode"
        ),
        BindingMethod(
            type = PhoneCodeView::class,
            attribute = "codeAttrChanged",
            method = "setListener"

        )
    ]
)
class PhoneCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    var touchListener: (() -> Unit)? = null

    private val selectedCode = MutableLiveData<String>()
    private lateinit var countriesAdapter: CountriesAdapter
    private lateinit var mDetector: GestureDetectorCompat
    private var listener: InverseBindingListener? = null

    init {
        View.inflate(context, R.layout.view_phone_code, this)
    }

    var code: String?
        get() {
            val code = (picker.selectedItem as Country).phoneCode
            return String.format(CODE_FORMAT, code)
        }
        set(value) {
            if(code != value && value != null) {
                selectByPhoneCode(value.toNumericCode())
            }
        }


    fun setListener(listener: InverseBindingListener) {
        this.listener = listener
    }

    fun getSelectedCode(): LiveData<String> {//todo remove
        return selectedCode
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        mDetector = GestureDetectorCompat(context, this)

        initCountryCodesDropDown()
        fetchCurrentCountryCode()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupDropDownWidth()
    }

    override fun getBaseline(): Int {
        return underline.baseline
    }

    private fun initCountryCodesDropDown() {
        val data = fetchCountryListWithReflection(context!!)
        countriesAdapter = CountriesAdapter(data)
        picker.adapter = countriesAdapter
        picker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val phoneCode = countriesAdapter.countries[p2].phoneCode

                listener?.onChange()
                selectedCode.postValue(String.format(CODE_FORMAT, phoneCode))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        picker.setOnTouchListener { view, motionEvent ->
            if(mDetector.onTouchEvent(motionEvent)) {
                touchListener?.invoke()
            }
            false
        }

        selectByCountyIsoCode(defaultCountyCode)
    }

    private fun fetchCurrentCountryCode() {
        val telephonyManager =
            context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val regionCode = telephonyManager.networkCountryIso
        selectByCountyIsoCode(regionCode)
    }

    private fun selectByCountyIsoCode(code: String) {
        var position = countriesAdapter.getPositionByIso(code)
        position = max(position, 0)
        picker.setSelection(position)
    }

    private fun selectByPhoneCode(code: String) {
        var position = countriesAdapter.getPositionByCode(code)
        position = max(position, 0)
        picker.setSelection(position)
    }

    private fun setupDropDownWidth() {
        var baseParent = this.parent!!
        while(baseParent.parent != null && baseParent.parent is View){
            baseParent = baseParent.parent
        }

        val baseView = baseParent as View

        baseView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                picker.dropDownWidth =
                    baseView.width - (2 * resources.getDimension(R.dimen.dialog_margin) + picker.x).toInt()
                requestLayout()
                baseView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    companion object {
        private const val defaultCountyCode = "1"
        private const val CODE_FORMAT = "+%s"
    }
}