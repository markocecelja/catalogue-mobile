package com.mcecelja.catalogue.data.dto.organization

import java.io.Serializable

data class OrganizationDTO(
    val id: Long,
    val name: String,
    val price: Float,
    val averageRating: Float,
    val currentUserRating: RatingDTO?
) : Serializable