package com.mcecelja.catalogue.data.dto.product

import com.mcecelja.catalogue.data.dto.organization.OrganizationWithPriceDTO

class ProductDTO(
    val id: Long,
    val name: String,
    val organizations: List<OrganizationWithPriceDTO>
)