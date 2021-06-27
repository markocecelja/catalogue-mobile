package com.mcecelja.catalogue.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.users.RegisterRequestDTO
import com.mcecelja.catalogue.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var loadingViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).also {
            loadingViewModel = it
        }

        binding.mbRegister.setOnClickListener { registerUser() }

        return binding.root
    }

    private fun registerUser() {

        if(!validateInputFields()) {
            return
        }

        val registerRequestDTO = RegisterRequestDTO(
            binding.etName.text.toString(),
            binding.etSurname.text.toString(),
            binding.etUsername.text.toString(),
            binding.etPassword.text.toString(),
            binding.etPasswordConfirm.text.toString(),
            binding.etEmail.text.toString(),
        )

        loadingViewModel.registerUser(requireActivity(), registerRequestDTO)
    }

    private fun validateInputFields(): Boolean {

        var isValid = true

        if (binding.etName.text.isNullOrEmpty()) {
            binding.tilName.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            binding.tilName.error = null
        }

        if (binding.etSurname.text.isNullOrEmpty()) {
            binding.tilSurname.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            binding.tilSurname.error = null
        }

        if (binding.etUsername.text.isNullOrEmpty()) {
            binding.tilUsername.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            binding.tilUsername.error = null
        }

        if (binding.etEmail.text.isNullOrEmpty()) {
            binding.tilEmail.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.tilPassword.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        if (binding.etPasswordConfirm.text.isNullOrEmpty()) {
            binding.tilPasswordConfirm.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            binding.tilPasswordConfirm.error = null
        }

        return isValid
    }

    companion object {
        const val TAG = "Register"
        fun create(): RegisterFragment {
            return RegisterFragment()
        }
    }
}