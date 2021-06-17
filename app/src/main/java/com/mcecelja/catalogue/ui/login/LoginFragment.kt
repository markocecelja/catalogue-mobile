package com.mcecelja.catalogue.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.UserLoginRequestDTO
import com.mcecelja.catalogue.data.dto.users.UserLoginResponseDTO
import com.mcecelja.catalogue.databinding.FragmentLoginBinding
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.services.AuthenticationService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.catalogue.MainActivity
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    private val loadingViewModel by viewModel<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        })

        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)

        loginBinding.mbLogin.setOnClickListener { loginUser() }
        loginBinding.tvSignUp.setOnClickListener { openRegisterFragment() }

        loadingViewModel.loadingVisibility.observe(viewLifecycleOwner, { (activity as LoginActivity).setupLoadingScreen(it) })

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

        if(loginBinding.etUsername.text.isNullOrEmpty()) {
            loginBinding.etUsername.error = "Ovo polje je obavezno!"
            return
        }

        if(loginBinding.etPassword.text.isNullOrEmpty()) {
            loginBinding.etPassword.error = "Ovo polje je obavezno!"
            return
        }

        val userLoginRequestDTO = UserLoginRequestDTO(
            loginBinding.etUsername.text.toString(),
            loginBinding.etPassword.text.toString()
        )

        val apiCall =
            RestUtil.createService(AuthenticationService::class.java).loginUser(userLoginRequestDTO)

        loadingViewModel.changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<UserLoginResponseDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<UserLoginResponseDTO>>,
                response: Response<ResponseMessage<UserLoginResponseDTO>>
            ) {

                loadingViewModel.changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        PreferenceManager.savePreference(PreferenceEnum.TOKEN, response.body()?.payload?.jwt)
                        val mainIntent = Intent(Catalogue.application, MainActivity::class.java)
                        startActivity(mainIntent)
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<UserLoginResponseDTO>>,
                t: Throwable
            ) {
                loadingViewModel.changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "User login failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    companion object {
        const val TAG = "Login"
        fun create(): LoginFragment {
            return LoginFragment()
        }
    }
}