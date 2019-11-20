package com.doneit.ascend.presentation.login.models

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.login.utils.getNotNull

class ValidatableField {
    val observableField = ObservableField<String>()
    val observableError: LiveData<String?>
        get() {
            return errors
        }
    var validator: ((String) -> ValidationResult)? = null
    private var isCorrect = false
    val isValid: Boolean
        get() {
            return isCorrect
        }

    private val errors = MutableLiveData<String>()

    init {
        observableField.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (validator != null && sender != null) {
                    val validation = validator!!.invoke(observableField.getNotNull())
                    isCorrect = validation.isSussed
                    errors.postValue(validation.errors.firstOrNull())
                }
            }
        })
    }
}