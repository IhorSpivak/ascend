package com.doneit.ascend.presentation.main.create_group.price_picker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.set
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentPricePickerBinding
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.utils.extensions.focusRequest
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.template_card_field.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.text.DecimalFormat
import java.text.NumberFormat

class PricePickerFragment(
    private val editor: TextInputEditText
) : BaseFragment<FragmentPricePickerBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<PricePickerContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: PricePickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        //editor.addTextChangedListener(textChanger)
        binding.apply {
            background = when(viewModel.createGroupModel.groupType){
                GroupType.SUPPORT -> resources.getColor(R.color.support_color)
                GroupType.INDIVIDUAL -> resources.getColor(R.color.master_mind_color)
                GroupType.MASTER_MIND -> resources.getColor(R.color.master_mind_color)
                GroupType.WEBINAR -> resources.getColor(R.color.background_dimmed)
                else -> resources.getColor(R.color.support_color)
            }
            btnCancel.setOnClickListener {
                viewModel.createGroupModel.price.observableField.get().let {
                    if (it!!.isNotEmpty()){
                        editor.text.apply {
                            this?.clear()
                            this?.append(it)
                        }
                        viewModel.backClick()
                    }else{
                        editor.text?.clear()
                        viewModel.backClick()
                    }
                }
                editor.clearFocus()
            }

            btnDone.setOnClickListener {
                viewModel.okPriceClick(editor.text.toString())
            }
            keyBackspace.setOnClickListener(clickHandle)
            keyDot.setOnClickListener(clickHandle)
            key0.setOnClickListener(clickHandle)
            key1.setOnClickListener(clickHandle)
            key2.setOnClickListener(clickHandle)
            key3.setOnClickListener(clickHandle)
            key4.setOnClickListener(clickHandle)
            key5.setOnClickListener(clickHandle)
            key6.setOnClickListener(clickHandle)
            key7.setOnClickListener(clickHandle)
            key8.setOnClickListener(clickHandle)
            key9.setOnClickListener(clickHandle)
        }
    }

    private val clickHandle = View.OnClickListener {
        if (it.id == R.id.key_backspace) {
            editor.text.apply {
                this?.length.let {length ->
                    if (length!! > 0){
                        this?.delete(length-1, length)
                    }
                }
            }
        } else {
            editor.text?.append((it as TextView).text)
        }
    }

    /*private val textChanger = object : TextWatcher{
        private var editing = false
        override fun afterTextChanged(p0: Editable?) {
            if (!editing) {
                editing = true
                p0?.replace(0,p0.length, CURRENCY_FORMAT.format(p0.toString().toDouble()).drop(1))
                editing = false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //empty
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //empty
        }
    }*/

    companion object {
        private val CURRENCY_FORMAT = NumberFormat.getCurrencyInstance()
        private const val PRICE_MASK = "[0999]{.}[09]"
    }
}