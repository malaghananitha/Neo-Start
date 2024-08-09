package com.example.neostart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.neostart.R
import com.example.neostart.databinding.FragmentAddressBinding
import com.example.neostart.model.AddressEntity
import com.example.neostart.model.EducationEntity
import com.example.neostart.model.ProfessionalEntity
import com.example.neostart.model.RegisterEntity
import com.example.neostart.util.DialogUtils
import com.example.neostart.util.DropdownData
import com.example.neostart.viewmodel.AddressViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val addressViewModel: AddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            addressViewModel = this@AddressFragment.addressViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the data passed from InfoFragment
        val registerEntity: RegisterEntity = arguments?.getParcelable("registerEntity")!!
        val educationEntity: EducationEntity = arguments?.getParcelable("educationEntity")!!
        val professionalEntity: ProfessionalEntity = arguments?.getParcelable("professionalEntity")!!

        with(binding.inclFragmentAddressToolbar) {
            toolbarTitle.text = getString(R.string.your_address)
            backpageButtonIcon.visibility = View.VISIBLE
            backpageButtonIcon.setOnClickListener {
                onBackPressed()
            }
        }

        with(binding) {
            addressViewModel?.let { viewModel ->
                etAddress.addTextChangedListener(GenericTextWatcher(viewModel.address))
                etLandmark.addTextChangedListener(GenericTextWatcher(viewModel.landmark))
                etCity.addTextChangedListener(GenericTextWatcher(viewModel.city))
                actvState.addTextChangedListener(GenericTextWatcher(viewModel.state))
                etPinCode.addTextChangedListener(GenericTextWatcher(viewModel.pinCode))
            }
        }

        setupStateDropdown()
        setupSubmitButton(registerEntity, educationEntity, professionalEntity)

        addressViewModel.isFormValid.observe(viewLifecycleOwner) { isValid ->
            binding.btnSubmit.isEnabled = isValid
        }
    }

    private fun setupStateDropdown() {
        DropdownData.setupAutoCompleteTextView(
            requireContext(),
            binding.actvState,
            DropdownData.getStateArray(requireContext()),
            R.drawable.edittext_background
        )
    }

    private fun setupSubmitButton(
        registerEntity: RegisterEntity,
        educationEntity: EducationEntity,
        professionalEntity: ProfessionalEntity
    ) {
        binding.btnSubmit.setOnClickListener {
            addressViewModel.validateForm()
            if (addressViewModel.isFormValid.value == true) {
                val addressEntity = AddressEntity(
                    address = addressViewModel.address.value ?: "",
                    landmark = addressViewModel.landmark.value ?: "",
                    city = addressViewModel.city.value ?: "",
                    state = addressViewModel.state.value ?: "",
                    pinCode = addressViewModel.pinCode.value ?: "",
                    registerId = registerEntity.id
                )

                lifecycleScope.launch {
                    val success = async { addressViewModel.insertAll(
                        registerEntity,
                        educationEntity,
                        professionalEntity,
                        addressEntity
                    ) }.await()
                    if (success) {
                        DialogUtils.showAlert(
                            context = requireContext(), title = "Successful",
                            message = "Record inserted successfully!"
                        )

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to insert data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill out the form correctly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onBackPressed() {
        if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
