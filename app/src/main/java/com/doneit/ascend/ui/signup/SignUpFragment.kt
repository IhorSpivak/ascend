package com.doneit.ascend.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.doneit.ascend.R
import com.doneit.ascend.databinding.SignUpFragmentBinding
import com.doneit.ascend.ui.BaseFragment
import com.google.gson.annotations.SerializedName

class Registration(
    var email: String = "",
    @SerializedName("phone_number") var phone: String = "",
    @SerializedName("full_name") var name: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("password_confirmation") var passwordConfirmation: String = ""
)

class ConfirmPhone(
    @SerializedName("Session-Token") val token: String,
    @SerializedName("code") val code: String
)

class SignUpFragment : BaseFragment() {

    lateinit var binder: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SignUpFragmentBinding.inflate(inflater, container, false)
        .apply {
            binder = this
            lifecycleOwner = this@SignUpFragment
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model =  ViewModelProvider(this).get(SignUpViewModel::class.java)
        binder.model = model

        model.registrationEvent.observe(this, Observer { success ->
            findNavController().navigate(R.id.loginFragment)
        })

    }
}
