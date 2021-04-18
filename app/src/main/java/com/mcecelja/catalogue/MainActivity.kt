package com.mcecelja.catalogue

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.UserLoginRequestDTO
import com.mcecelja.catalogue.data.dto.users.UserLoginResponseDTO
import com.mcecelja.catalogue.databinding.ActivityMainBinding
import com.mcecelja.catalogue.services.AuthenticationService
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.bLogin.setOnClickListener { loginUser() }
    }

    private fun setUpApiCall(userLoginRequestDTO: UserLoginRequestDTO) {
        val apiCall =
            RestUtil.createService(AuthenticationService::class.java).loginUser(userLoginRequestDTO)

        apiCall.enqueue(object : Callback<ResponseMessage<UserLoginResponseDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<UserLoginResponseDTO>>,
                response: Response<ResponseMessage<UserLoginResponseDTO>>
            ) {
                if (response.isSuccessful) {
                    PreferenceManager.savePreference("Token", response.body()?.payload?.jwt)
                }
            }

            override fun onFailure(call: Call<ResponseMessage<UserLoginResponseDTO>>, t: Throwable) {
                Toast.makeText(
                    Catalogue.application,
                    "User login failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun loginUser() {
        val userLoginRequestDTO = UserLoginRequestDTO(
            mainBinding.etUsername.text.toString(),
            mainBinding.etPassword.text.toString()
        )
        setUpApiCall(userLoginRequestDTO)
    }

}