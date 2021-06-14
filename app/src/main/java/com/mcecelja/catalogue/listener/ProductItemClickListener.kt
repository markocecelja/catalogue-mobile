package com.mcecelja.catalogue.listener

import com.mcecelja.catalogue.databinding.ItemProductBinding

interface ProductItemClickListener {

    fun onFavouriteClicked(itemProductBinding: ItemProductBinding)
}