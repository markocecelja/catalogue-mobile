package com.mcecelja.catalogue.listener

import com.mcecelja.catalogue.data.dto.product.ProductDTO

interface FavouriteItemClickListener {

    fun onItemClicked(product: ProductDTO)
}