package com.mcecelja.catalogue.ui.login

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.RegisterRequestDTO
import com.mcecelja.catalogue.data.dto.users.UserLoginRequestDTO
import com.mcecelja.catalogue.data.dto.users.UserLoginResponseDTO
import com.mcecelja.catalogue.enums.PreferenceEnum
import com.mcecelja.catalogue.services.AuthenticationService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.ui.catalogue.CatalogueActivity
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : LoadingViewModel() {

    fun loginUser(activity: Activity, userLoginRequestDTO: UserLoginRequestDTO) {

        val apiCall =
            RestUtil.createService(AuthenticationService::class.java).loginUser(userLoginRequestDTO)

        changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<UserLoginResponseDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<UserLoginResponseDTO>>,
                response: Response<ResponseMessage<UserLoginResponseDTO>>
            ) {

                changeVisibility(View.INVISIBLE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        PreferenceManager.savePreference(
                            PreferenceEnum.TOKEN,
                            response.body()?.payload?.jwt
                        )
                        val mainIntent = Intent(Catalogue.application, CatalogueActivity::class.java)
                        activity.startActivity(mainIntent)
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<UserLoginResponseDTO>>,
                t: Throwable
            ) {
                changeVisibility(View.INVISIBLE)

                Toast.makeText(
                    Catalogue.application,
                    "User login failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    fun registerUser(activity: Activity, registerRequestDTO: RegisterRequestDTO) {

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
                        loginUser(
                            activity,
                            UserLoginRequestDTO(
                                registerRequestDTO.username,
                                registerRequestDTO.password
                            )
                        )
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
}