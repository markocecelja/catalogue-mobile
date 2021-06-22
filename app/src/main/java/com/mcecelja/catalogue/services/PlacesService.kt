package com.mcecelja.catalogue.services

import com.mcecelja.catalogue.data.dto.places.CoordinatesDTO
import com.mcecelja.catalogue.data.dto.places.PlacesResponseDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {

    @GET("nearbysearch/json")
    fun getNearbyPlaces(
        @Query("location") location: CoordinatesDTO,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("keyword") name: String,
        @Query("key") key: String
    ): Call<PlacesResponseDTO>
}