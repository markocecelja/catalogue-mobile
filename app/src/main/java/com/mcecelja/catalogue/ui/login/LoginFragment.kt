package com.mcecelja.catalogue.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.users.UserLoginRequestDTO
import com.mcecelja.catalogue.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    private lateinit var loadingViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        ViewModelProvider(requireActivity()).get(LoginViewModel::class.java).also {
            loadingViewModel = it
        }

        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)

        loginBinding.mbLogin.setOnClickListener { loginUser() }
        loginBinding.tvSignUp.setOnClickListener { openRegisterFragment() }

        loadingViewModel.loadingVisibility.observe(
            viewLifecycleOwner,
            { (activity as LoginActivity).setupLoadingScreen(it) })

        return loginBinding.root
    }

    private fun openRegisterFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fl_fragmentContainer,
                RegisterFragment.create(),
                RegisterFragment.TAG
            )
            .addToBackStack(TAG)
            .commit()
    }

    private fun loginUser() {

        if(!validateInputFields()) {
            return
        }

        val userLoginRequestDTO = UserLoginRequestDTO(
            loginBinding.etUsername.text.toString(),
            loginBinding.etPassword.text.toString()
        )

        loadingViewModel.loginUser(requireActivity(), userLoginRequestDTO)
    }

    private fun validateInputFields(): Boolean {

        var isValid = true

        if (loginBinding.etUsername.text.isNullOrEmpty()) {
            loginBinding.tilUsername.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            loginBinding.tilUsername.error = null
        }

        if (loginBinding.etPassword.text.isNullOrEmpty()) {
            loginBinding.tilPassword.error = getString(R.string.mandatory_field)
            isValid = false
        } else {
            loginBinding.tilPassword.error = null
        }

        return isValid
    }

    companion object {
        const val TAG = "Login"
        fun create(): LoginFragment {
            return LoginFragment()
        }
    }
}