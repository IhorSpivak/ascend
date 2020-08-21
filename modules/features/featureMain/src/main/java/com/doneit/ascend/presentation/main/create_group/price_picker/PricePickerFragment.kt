package com.doneit.ascend.presentation.main.create_group.price_picker

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.databinding.FragmentPricePickerBinding
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

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
        binding.apply {
            background = when (viewModel.createGroupModel.groupType) {
                GroupType.SUPPORT -> resources.getColor(R.color.support_color)
                GroupType.INDIVIDUAL -> resources.getColor(R.color.master_mind_color)
                GroupType.MASTER_MIND -> resources.getColor(R.color.master_mind_color)
                GroupType.WEBINAR -> resources.getColor(R.color.red_webinar_color)
                else -> resources.getColor(R.color.support_color)
            }
            btnCancel.setOnClickListener {
                viewModel.createGroupModel.price.observableField.get().let {
                    if (it!!.isNotEmpty()) {
                        editor.text.apply {
                            this?.clear()
                            this?.append(it)
                        }
                        viewModel.backClick()
                    } else {
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
                this?.length.let { length ->
                    if (length!! > 0 && editor.selectionStart > 0) {
                        this?.delete(editor.selectionStart - 1, editor.selectionStart)
                    }
                }
            }
        } else {
            editor.text?.apply {
                insert(editor.selectionStart, (it as TextView).text);
            }
        }
    }
}