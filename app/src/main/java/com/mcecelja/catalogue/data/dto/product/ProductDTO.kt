package com.mcecelja.catalogue.data.dto.product

import com.mcecelja.catalogue.data.dto.organization.OrganizationWithPriceDTO
import java.io.Serializable

class ProductDTO(
    val id: Long,
    val name: String,
    val organizations: List<OrganizationWithPriceDTO>,
    val currentUserFavourite: Boolean
) : Serializable