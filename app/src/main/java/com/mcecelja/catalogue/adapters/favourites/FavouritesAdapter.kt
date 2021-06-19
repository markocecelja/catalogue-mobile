package com.mcecelja.catalogue.adapters.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.listener.FavouriteItemClickListener

class FavouritesAdapter(products: List<ProductDTO>, private val favouriteItemClickListener: FavouriteItemClickListener) : RecyclerView.Adapter<FavouritesViewHolder>() {

    private val products: MutableList<ProductDTO> = mutableListOf()
    init {
        refreshData(products)
    }

    fun refreshData(products: List<ProductDTO>) {
        this.products.clear()
        this.products.addAll(products)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite, parent, false)

        return FavouritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, favouriteItemClickListener, position)

    }

    override fun getItemCount(): Int {
        return products.size
    }
}