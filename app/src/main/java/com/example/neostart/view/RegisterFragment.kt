package com.example.neostart.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.neostart.R
import com.example.neostart.databinding.FragmentRegisterBinding
import com.example.neostart.model.RegisterEntity
import com.example.neostart.viewmodel.RegisterViewModel
import java.io.IOException

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel:RegisterViewModel by viewModels()
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var photoURI: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            viewModel = this@RegisterFragment.registerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        with(binding.inclFragmentRegisterToolbar) {
            toolbarTitle.text = getString(R.string.register)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel.profilePicClick.observe(viewLifecycleOwner, Observer {
            showImagePickerDialog()
        })
        with(binding) {
            viewModel?.let {
                etFirstName.addTextChangedListener(GenericTextWatcher(it.firstName))
                etLastName.addTextChangedListener(GenericTextWatcher(it.lastName))
                etEmail.addTextChangedListener(GenericTextWatcher(it.email))
                etPassword.addTextChangedListener(GenericTextWatcher(it.password))
                etConfirmPassword.addTextChangedListener(GenericTextWatcher(it.confirmPassword))
                etPhoneNumber.addTextChangedListener(GenericTextWatcher(it.mobileNumber))
            }
        }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.extras?.get("data")?.let { data ->
                        binding.ivProfilePic.setImageBitmap(data as Bitmap)
                    } ?: photoURI?.let { uri ->
                        binding.ivProfilePic.setImageURI(uri)
                    }
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val selectedImageUri = result.data?.data
                    binding.ivProfilePic.setImageURI(selectedImageUri)
                }
            }

        binding.btnNext.setOnClickListener {
            if (registerViewModel.isFormValid(requireContext())) {
                val registerEntity = RegisterEntity(
                    firstName = registerViewModel.firstName.value ?: "",
                    lastName = registerViewModel.lastName.value ?: "",
                    email = registerViewModel.email.value ?: "",
                    password = registerViewModel.password.value ?: "",
                    confirmPassword = registerViewModel.confirmPassword.value ?: "",
                    mobileNumber = registerViewModel.mobileNumber.value ?: "",
                    gender = registerViewModel.gender.value ?: "",
                    currentPhotoPath = registerViewModel.currentPhotoPath
                )

                val bundle = Bundle()
                bundle.putParcelable("registerEntity", registerEntity)

                val infoFragment = InfoFragment()
                infoFragment.arguments = bundle
                // Begin a fragment transaction
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        infoFragment
                    ) // Replace 'fragment_container' with your actual container ID
                    .addToBackStack(null) // Optional: Add this fragment transaction to the back stack
                    .commit() //
            }
        }

        binding.rbMale.setOnClickListener {
            registerViewModel.gender.value = "Male"
        }

        binding.rbFemale.setOnClickListener {
            registerViewModel.gender.value = "Female"
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Select Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> dispatchTakePictureIntent()
                1 -> dispatchChooseFromGalleryIntent()
            }
        }
        builder.show()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager).also {
                try {
                    val file = registerViewModel.createImageFile(requireContext())
                    photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.neostart.fileprovider",
                        file
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLauncher.launch(takePictureIntent)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun dispatchChooseFromGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
}
