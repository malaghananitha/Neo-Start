package com.example.neostart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddressViewModel : ViewModel() {

    val address = MutableLiveData<String>()
    val landmark = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val pinCode = MutableLiveData<String>()

    private val formValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = formValid

    fun validateForm() {
        formValid.value = isAddressValid() &&
                isLandmarkValid() &&
                isCityValid() &&
                isStateValid() &&
                isPinCodeValid()
    }

    private fun isAddressValid(): Boolean {
        return !address.value.isNullOrEmpty() && address.value!!.length > 3
    }

    private fun isLandmarkValid(): Boolean {
        return !landmark.value.isNullOrEmpty() && landmark.value!!.length > 3
    }

    private fun isCityValid(): Boolean {
        return !city.value.isNullOrEmpty() && city.value!!.all { it.isLetter() }
    }

    private fun isStateValid(): Boolean {
        return !state.value.isNullOrEmpty()
    }

    private fun isPinCodeValid(): Boolean {
        return !pinCode.value.isNullOrEmpty() && pinCode.value!!.length == 6 && pinCode.value!!.all { it.isDigit() }
    }
}
