package com.example.neostart.ui

import android.graphics.drawable.Drawable
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.neostart.R
import com.example.neostart.databinding.FragmentInfoBinding
import com.example.neostart.util.DropdownData
import com.example.neostart.viewmodel.InfoViewModel

class InfoFragment : Fragment() {

    companion object {
        fun newInstance() = InfoFragment()
    }

    private val viewModel: InfoViewModel by viewModels()
    private lateinit var binding:FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        with(binding.inclFragmentInfoToolbar) {
            toolbarTitle.text = getString(R.string.your_info)
            backpageButtonIcon.visibility = View.VISIBLE
            backpageButtonIcon.setOnClickListener(){
              onBackPressed()
            }
            binding.btnPrevious.setOnClickListener(){
                onBackPressed()
            }
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()


        with(binding) {
            viewModel?.let {
                actvQualification.addTextChangedListener(GenericTextWatcher(it.qualification))
                actvYearOfPassing.addTextChangedListener(GenericTextWatcher(it.yearOfPassing))
                etGrade.addTextChangedListener(GenericTextWatcher(it.grade))
                etExperience.addTextChangedListener(GenericTextWatcher(it.experience))
                actvDesignation.addTextChangedListener(GenericTextWatcher(it.designation))
                actvDomain.addTextChangedListener(GenericTextWatcher(it.domain))
                // Assuming you have EditText fields for these
            }
        }
        binding.btnNext.setOnClickListener {
            if (viewModel.isFormValid(requireContext())) {
                // Create an instance of InfoFragment
                val addressFragment = AddressFragment()

                // Begin a fragment transaction
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, addressFragment) // Replace 'fragment_container' with your actual container ID
                    .addToBackStack(null) // Optional: Add this fragment transaction to the back stack
                    .commit() // Commit the transaction
            }

         }
    }

    private fun setupDropdowns() {
        DropdownData.setupAutoCompleteTextView(
            requireContext(),
            binding.actvQualification,
            DropdownData.getEducationArray(requireContext()),
            R.drawable.edittext_background
        )
        DropdownData.setupAutoCompleteTextView(
            requireContext(),
            binding.actvYearOfPassing,
            DropdownData.getYearOfPassingArray(requireContext()),
            R.drawable.edittext_background
        )
        DropdownData.setupAutoCompleteTextView(
            requireContext(),
            binding.actvDesignation,
            DropdownData.getDesignationArray(requireContext()),
            R.drawable.edittext_background
        )
        DropdownData.setupAutoCompleteTextView(
            requireContext(),
            binding.actvDomain,
            DropdownData.getDomainArray(requireContext()),
            R.drawable.edittext_background
        )
    }

    private fun onBackPressed() {
        if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            requireActivity().supportFragmentManager.popBackStack()
        } else {
           // Call the super method to handle default back behavior
        }
    }
}