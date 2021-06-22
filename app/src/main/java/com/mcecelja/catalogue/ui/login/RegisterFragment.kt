package com.mcecelja.catalogue.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.RegisterRequestDTO
import com.mcecelja.catalogue.databinding.FragmentRegisterBinding
import com.mcecelja.catalogue.services.AuthenticationService
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)

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

        val apiCall =
            RestUtil.createService(AuthenticationService::class.java)
                .registerUser(registerRequestDTO)

        apiCall.enqueue(object : Callback<ResponseMessage<String>> {
            override fun onResponse(
                call: Call<ResponseMessage<String>>,
                response: Response<ResponseMessage<String>>
            ) {
                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fl_fragmentContainer,
                                LoginFragment.create(),
                                LoginFragment.TAG
                            )
                            .addToBackStack(TAG)
                            .commit()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseMessage<String>>, t: Throwable) {
                Toast.makeText(
                    Catalogue.application,
                    "User registration failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
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