package com.mcecelja.catalogue.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["organizationId", "productId"]
)
data class OrganizationProduct (
    val organizationId: Long,
    val productId: Long,
    val price: Float
)