package com.mcecelja.catalogue.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithOrganizations(
    @Embedded
    val organizationProduct: OrganizationProduct,

    @Relation(
        entity = Product::class,
        entityColumn = "id",
        parentColumn = "product_id"
    )
    val product: Product,

    @Relation(
        entity = Organization::class,
        entityColumn = "id",
        parentColumn = "organization"
    )
    var organization: Organization
) {
}