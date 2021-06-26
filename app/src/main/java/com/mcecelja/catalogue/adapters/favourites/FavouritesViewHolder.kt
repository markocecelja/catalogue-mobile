package com.mcecelja.catalogue.adapters.favourites

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.databinding.ItemFavouriteBinding
import com.mcecelja.catalogue.listener.FavouriteItemClickListener

class FavouritesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(
        product: ProductDTO,
        favouriteItemClickListener: FavouriteItemClickListener,
        position: Int
    ) {
        val itemBinding = ItemFavouriteBinding.bind(itemView)
        itemBinding.tvProductName.text = product.name

        itemView.setOnClickListener{ favouriteItemClickListener.onItemClicked(product) }
    }
}