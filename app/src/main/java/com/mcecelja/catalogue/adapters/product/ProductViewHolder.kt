package com.mcecelja.catalogue.adapters.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.ItemProductBinding
import com.mcecelja.catalogue.listener.ProductItemClickListener

class ProductViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(product: ProductDTO, productItemClickListener: ProductItemClickListener) {
        val itemBinding = ItemProductBinding.bind(itemView)
        itemBinding.tvProductName.text = product.name
        itemBinding.ivFavourite.setOnClickListener { productItemClickListener.onFavouriteClicked(itemBinding) }
    }
}