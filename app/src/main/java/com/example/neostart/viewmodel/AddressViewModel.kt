package com.example.neostart.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.neostart.db.NeoStartDatabase
import com.example.neostart.model.AddressEntity
import com.example.neostart.model.EducationEntity
import com.example.neostart.model.ProfessionalEntity
import com.example.neostart.model.RegisterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddressViewModel(application: Application) :AndroidViewModel(application) {

    private val registerDao = NeoStartDatabase.getDatabase(application).registerDao()
    private val educationDao = NeoStartDatabase.getDatabase(application).educationDao()
    private val professionalDao = NeoStartDatabase.getDatabase(application).professionalDao()
    private val addressDao = NeoStartDatabase.getDatabase(application).addressDao()

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

    suspend fun insertAll(
        registerEntity: RegisterEntity,
        educationEntity: EducationEntity,
        professionalEntity: ProfessionalEntity,
        addressEntity: AddressEntity
    ): Boolean {
        return try {
            viewModelScope.launch(Dispatchers.IO) {
                val registerId = async { registerDao.insert(registerEntity) }.await()
                val updatedEducationEntity = educationEntity.copy(registerId = registerId)
                val updatedProfessionalEntity = professionalEntity.copy(registerId = registerId)
                val updatedAddressEntity = addressEntity.copy(registerId = registerId)

                educationDao.insert(updatedEducationEntity)
                professionalDao.insert(updatedProfessionalEntity)
                addressDao.insert(updatedAddressEntity)
            }.join()
            true
        } catch (e: Exception) {
            false
        }
    }
}
