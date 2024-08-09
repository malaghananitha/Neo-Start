package com.example.neostart.viewmodel

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.neostart.db.NeoStartDatabase
import com.example.neostart.util.DialogUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RegisterViewModel(application: Application):AndroidViewModel(application) {

    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()
    var mobileNumber = MutableLiveData<String>()
    var profilePicClick = MutableLiveData<Unit>()
    var gender = MutableLiveData<String>()
    var currentPhotoPath: String? = null

    private val registerDao = NeoStartDatabase.getDatabase(application).registerDao()

    private val passwordPattern = Pattern.compile(
        "^(?=.*[0-9])(?=.*[!@#\$%^&*])(?=\\S+$).{6,}$"
    )

    fun onProfilePicClicked() {
        profilePicClick.value = Unit
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun isFormValid(context: Context): Boolean {
        if (firstName.value.isNullOrEmpty() || firstName.value!!.length < 3) {
            showValidationErrors("First name must be more than 3 characters", context)
            return false
        }

        if (lastName.value.isNullOrEmpty() || lastName.value!!.length < 3) {
            showValidationErrors("Last name must be more than 3 characters", context)
            return false
        }

        if (email.value.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()) {
            showValidationErrors("Invalid email address", context)
            return false
        }

        if (password.value.isNullOrEmpty() || !passwordPattern.matcher(password.value!!).matches()) {
            showValidationErrors("Password must contain at least 1 number, 1 special character, and be at least 6 characters long", context)
            return false
        }

        if (confirmPassword.value.isNullOrEmpty() || confirmPassword.value != password.value) {
            showValidationErrors("Passwords do not match", context)
            return false
        }

        if (mobileNumber.value.isNullOrEmpty() || !mobileNumber.value!!.matches(Regex("^\\d{10}$"))) {
            showValidationErrors("Invalid mobile number", context)
            return false
        }

        if (gender.value.isNullOrEmpty()) {
            showValidationErrors("Gender is required", context)
            return false
        }

        return true
    }

    fun showValidationErrors(errorMessage: String, context: Context) {
        DialogUtils.showAlert(
            context = context,
            title = "Validation Error",
            message = errorMessage,
            positiveButtonText = "OK",
        )
    }


}
