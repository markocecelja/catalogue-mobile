package com.mcecelja.catalogue.data.dto.product

import java.io.Serializable

class ProductDTO(
    val id: Long,
    val name: String,
    val currentUserFavourite: Boolean
) : Serializable