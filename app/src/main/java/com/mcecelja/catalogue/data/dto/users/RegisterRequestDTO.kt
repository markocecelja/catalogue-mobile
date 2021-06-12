package com.mcecelja.catalogue.data.dto.users

data class RegisterRequestDTO(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val confirmationPassword: String,
    val email: String,
    )