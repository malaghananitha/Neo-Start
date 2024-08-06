package com.example.neostart.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostart.util.DialogUtils

class InfoViewModel : ViewModel() {
    val qualification = MutableLiveData<String>()
    val yearOfPassing = MutableLiveData<String>()
    val grade = MutableLiveData<String>()
    val experience = MutableLiveData<String>()
    val designation = MutableLiveData<String>()
    val domain = MutableLiveData<String>()


    fun isFormValid(context: Context): Boolean {
        // Validate qualification (required)
        if (qualification.value.isNullOrEmpty()) {
            showValidationError(context, "Qualification is required")
            return false
        }

        // Validate yearOfPassing (required, numbers only)
        if (yearOfPassing.value.isNullOrEmpty() || !yearOfPassing.value!!.matches(Regex("^\\d+$"))) {
            showValidationError(context, "Year Of Passing must be a number and is required")
            return false
        }

        // Validate grade (optional, can contain characters and numbers)
        // regex for float value "^[a-zA-Z0-9.\\s]+$"
        if (!grade.value.isNullOrEmpty() && !grade.value!!.matches(Regex("^[a-zA-Z0-9]+$"))) {
            showValidationError(context, "Grade can only contain letters and numbers")
            return false
        }

        // Validate experience (required, numbers only)
        if (experience.value.isNullOrEmpty() || !experience.value!!.matches(Regex("^\\d+$"))) {
            showValidationError(context, "Experience must be a number and is required")
            return false
        }

        // Validate designation (required, can contain characters and numbers)
        if (designation.value.isNullOrEmpty() || !designation.value!!.matches(Regex("^[a-zA-Z0-9\\s]+$"))) {
            showValidationError(context, "Designation is required and can only contain letters, numbers, and spaces")
            return false
        }

        // Validate domain (required, can contain characters and numbers)
        if (domain.value.isNullOrEmpty() || !domain.value!!.matches(Regex("^[a-zA-Z0-9\\s]+$"))) {
            showValidationError(context, "Domain is required and can only contain letters, numbers, and spaces")
            return false
        }

        return true
    }

    private fun showValidationError(context: Context, errorMessage: String) {
        DialogUtils.showAlert(
            context = context,
            title = "Validation Error",
            message = errorMessage,
            positiveButtonText = "OK"
        )
    }
    fun onPreviousClicked() {
        // Handle previous button click
    }

    fun onNextClicked() {
        // Handle next button click
    }
}
