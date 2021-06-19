package com.mcecelja.catalogue.model

data class LocationModel(
    val longitude: Double,
    val latitude: Double,
    val country: String,
    val city: String,
    val street: String,
    val homeNumber: String
)