package com.mcecelja.catalogue.adapters.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcecelja.catalogue.R
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.listener.ProductItemClickListener

class ProductAdapter(products: List<ProductDTO>, private val productItemClickListener: ProductItemClickListener) : RecyclerView.Adapter<ProductViewHolder>() {

    private val products: MutableList<ProductDTO> = mutableListOf()
    init {
        refreshData(products)
    }

    fun refreshData(products: List<ProductDTO>) {
        this.products.clear()
        this.products.addAll(products)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, productItemClickListener, position)

    }

    override fun getItemCount(): Int {
        return products.size
    }
}