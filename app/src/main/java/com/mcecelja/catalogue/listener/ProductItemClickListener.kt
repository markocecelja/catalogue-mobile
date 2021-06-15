package com.mcecelja.catalogue.listener

interface ProductItemClickListener {

    fun onFavouriteClicked(position: Int)

    fun onProductClicked(position: Int)
}