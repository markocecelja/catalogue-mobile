package com.mcecelja.catalogue.services

import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.RegisterRequestDTO
import com.mcecelja.catalogue.data.dto.users.UserLoginRequestDTO
import com.mcecelja.catalogue.data.dto.users.UserLoginResponseDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    @POST("authentication/login")
    fun loginUser(@Body userLoginRequestDTO: UserLoginRequestDTO): Call<ResponseMessage<UserLoginResponseDTO>>

    @POST("authentication/registration")
    fun registerUser(@Body registerRequestDTO: RegisterRequestDTO): Call<ResponseMessage<String>>
}