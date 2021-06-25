package com.mcecelja.catalogue.services

import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import com.mcecelja.catalogue.data.dto.organization.RatingDTO
import retrofit2.Call
import retrofit2.http.*

interface OrganizationService {

    @PUT("organizations/{id}/recension")
    fun leaveRecension(
        @Path("id") id: Long,
        @Body rating: RatingDTO
    ): Call<ResponseMessage<OrganizationDTO>>

    @GET("organizations/currentUserRated")
    fun getCurrentUserRatedOrganizations(): Call<ResponseMessage<List<OrganizationDTO>>>

    @GET("organizations")
    fun getOrganizations(
        @Query("productId") productId: Long?,
    ): Call<ResponseMessage<List<OrganizationDTO>>>
}