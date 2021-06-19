package com.mcecelja.catalogue.data.dto.product

import com.mcecelja.catalogue.data.dto.organization.OrganizationDTO
import java.io.Serializable

class ProductDTO(
    val id: Long,
    val name: String,
    val organizations: List<OrganizationDTO>,
    val currentUserFavourite: Boolean
) : Serializable