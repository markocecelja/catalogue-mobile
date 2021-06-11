package com.mcecelja.catalogue.services

import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.users.UserDTO
import retrofit2.Call
import retrofit2.http.GET

interface UserService {

    @GET("users/current")
    fun getCurrentUserInfo(): Call<ResponseMessage<UserDTO>>
}