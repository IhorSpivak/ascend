package com.doneit.ascend.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.FrameLayout
import android.widget.TextView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.CustomPriceInputBinding

class PriceKeyboard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var inputConnection: InputConnection

    private lateinit var binding: CustomPriceInputBinding

    init {
        View.inflate(context, R.layout.custom_price_input, this)
        binding = CustomPriceInputBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        binding.apply {
            key0.setOnClickListener(clickHandle)
            key1.setOnClickListener(clickHandle)
            key2.setOnClickListener(clickHandle)
            key3.setOnClickListener { clickHandle }
            key4.setOnClickListener { clickHandle }
            key5.setOnClickListener { clickHandle }
            key6.setOnClickListener { clickHandle }
            key7.setOnClickListener { clickHandle }
            key8.setOnClickListener { clickHandle }
            key9.setOnClickListener { clickHandle }
        }
    }
    private val clickHandle = OnClickListener {
        if(it.id == R.id.key_backspace){
             inputConnection.deleteSurroundingText(1,0)
        }else{
            inputConnection.commitText((it as TextView).text, 1)
        }
    }
}