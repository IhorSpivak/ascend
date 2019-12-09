package com.doneit.ascend.presentation.main.models

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.presentation.utils.getNotNull

class ValidatableField {
    val observableField = ObservableField<String?>("")
    val observableError: LiveData<Int?>
        get() {
            return errors
        }
    var validator: ((String) -> ValidationResult)? = null
    var onFieldInvalidate: (() -> Unit)? = null
    val isValid: Boolean
        get() {
            return isCorrect
        }

    private var isCorrect = false
    private val errors = MutableLiveData<Int?>()

    init {
        observableField.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (validator != null && sender != null) {
                    val validation = validator!!.invoke(observableField.getNotNull())
                    val oldCorrect = isCorrect
                    isCorrect = validation.isSussed
                    if (oldCorrect != isCorrect) {
                        onFieldInvalidate?.invoke()
                    }

                    errors.postValue(validation.errors.firstOrNull())
                }
            }
        })
    }

    fun removeError() {
        errors.postValue(null)
    }
}