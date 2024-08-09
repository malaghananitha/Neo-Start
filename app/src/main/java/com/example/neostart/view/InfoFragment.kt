package com.example.neostart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.neostart.R
import com.example.neostart.databinding.FragmentInfoBinding
import com.example.neostart.model.EducationEntity
import com.example.neostart.model.ProfessionalEntity
import com.example.neostart.model.RegisterEntity
import com.example.neostart.util.DropdownData
import com.example.neostart.viewmodel.InfoViewModel

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    private val infoViewModel: InfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.viewModel = infoViewModel
        binding.lifecycleOwner = this

        with(binding.inclFragmentInfoToolbar) {
            toolbarTitle.text = getString(R.string.your_info)
            backpageButtonIcon.visibility = View.VISIBLE
            backpageButtonIcon.setOnClickListener {
                onBackPressed()
            }
            binding.btnPrevious.setOnClickListener {
                onBackPressed()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()

        // Retrieve the RegisterEntity from the bundle
        val registerEntity: RegisterEntity? = arguments?.getParcelable("registerEntity")
        registerEntity?.let {
            infoViewModel.registerEntity = it
        }

        with(binding) {
            viewModel?.let {
                actvQualification.addTextChangedListener(GenericTextWatcher(it.qualification))
                actvYearOfPassing.addTextChangedListener(GenericTextWatcher(it.yearOfPassing))
                etGrade.addTextChangedListener(GenericTextWatcher(it.grade))
                etExperience.addTextChangedListener(GenericTextWatcher(it.experience))
                actvDesignation.addTextChangedListener(GenericTextWatcher(it.designation))
                actvDomain.addTextChangedListener(GenericTextWatcher(it.domain))
            }
        }
        binding.btnNext.setOnClickListener {
            if (infoViewModel.isFormValid(requireContext())) {

                val educationEntity = EducationEntity(
                    qualification = infoViewModel.qualification.value ?: "",
                    yearOfPassing = infoViewModel.yearOfPassing.value?.toInt()?:2000,
                    grade = infoViewModel.grade.value ?: "",
                    registerId = infoViewModel.registerEntity?.id ?: 1
                )

                val professionalEntity = ProfessionalEntity(
                    experience = infoViewModel.experience.value?.toInt()?:2000,
                    designation = infoViewModel.designation.value ?: "",
                    domain = infoViewModel.domain.value ?: "",
                    registerId = infoViewModel.registerEntity?.id ?: 1
                )
                val bundle = Bundle()
                bundle.putParcelable("registerEntity", registerEntity)
                bundle.putParcelable("educationEntity", educationEntity)
                bundle.putParcelable("professionalEntity",professionalEntity)

                val addressFragment = AddressFragment()
                addressFragment.arguments = bundle
                // Begin a fragment transaction
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                     addressFragment
                    ) // Replace 'fragment_container' with your actual container ID
                    .addToBackStack(null) // Optional: Add this fragment transaction to the back stack
                    .commit() //


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
            // Default back behavior
        }
    }
}
