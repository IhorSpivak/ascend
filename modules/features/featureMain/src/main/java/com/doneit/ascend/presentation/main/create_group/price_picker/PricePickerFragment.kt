package com.doneit.ascend.presentation.main.create_group.price_picker

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentPricePickerBinding
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.redmadrobot.inputmask.MaskedTextChangedListener
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class PricePickerFragment(
    val editor: EditText
) : BaseFragment<FragmentPricePickerBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<PricePickerContract.ViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }

    override val viewModel: PricePickerContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            background = when(viewModel.createGroupModel.groupType){
                GroupType.SUPPORT -> resources.getColor(R.color.support_color)
                GroupType.INDIVIDUAL -> resources.getColor(R.color.master_mind_color)
                GroupType.MASTER_MIND -> resources.getColor(R.color.master_mind_color)
                GroupType.WEBINAR -> resources.getColor(R.color.background_dimmed)
                else -> resources.getColor(R.color.support_color)
            }
            btnCancel.setOnClickListener {
                if(viewModel.createGroupModel.price.observableField.get()!!.isNotBlank()) {
                    viewModel.backClick()
                }else {
                    editor.text?.clear()
                    viewModel.backClick()
                }
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
        editor.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.priceOk.postValue("^\\d{1,4}(\\.\\d{1,4})?\$".toRegex().matches(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        hideKeyboard()
    }

    private val clickHandle = View.OnClickListener {
        if (it.id == R.id.key_backspace) {
            if(editor.text.isNotEmpty()) {
                editor.setText(editor.text.toString().dropLast(1))
            }
        } else {
            editor.text.append((it as TextView).text)
        }
    }

    companion object {
        private const val PRICE_MASK = "[0999]{.}[09]"
    }
}