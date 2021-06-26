package com.mcecelja.catalogue.listener

import com.mcecelja.catalogue.data.dto.product.ProductDTO

interface ProductItemClickListener {

    fun onFavouriteClicked(product: ProductDTO)

    fun onProductClicked(product: ProductDTO)
}