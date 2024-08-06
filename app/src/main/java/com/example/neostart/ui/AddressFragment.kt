package com.example.neostart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.neostart.R
import com.example.neostart.databinding.FragmentAddressBinding
import com.example.neostart.util.DropdownData
import com.example.neostart.viewmodel.AddressViewModel


class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val addressViewModel: AddressViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        binding = FragmentAddressBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addressViewModel = addressViewModel

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.inclFragmentAddressToolbar) {
            toolbarTitle.text = getString(R.string.your_address)
            backpageButtonIcon.visibility = View.VISIBLE
            backpageButtonIcon.setOnClickListener(){
                onBackPressed()
            }
        }

        setupStateDropdown()
        setupSubmitButton()

        addressViewModel.isFormValid.observe(viewLifecycleOwner, Observer { isValid ->
            binding.btnSubmit.isEnabled = isValid
        })
    }

    private fun setupStateDropdown() {
        DropdownData.setupAutoCompleteTextView(
            requireContext(),
            binding.autoCompleteTextViewState,
            DropdownData.getStateArray(requireContext()),
            R.drawable.edittext_background
        )
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            addressViewModel.validateForm()
            if (addressViewModel.isFormValid.value == true) {
                Toast.makeText(requireContext(), "Form Submitted Successfully!", Toast.LENGTH_SHORT)
                    .show()
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
        } else {
            // Call the super method to handle default back behavior
        }
    }


}
