package com.mcecelja.catalogue.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcecelja.catalogue.data.dto.product.ProductDTO

class ProductViewModel : ViewModel() {

    private val _products: MutableLiveData<List<ProductDTO>> = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    fun setProducts(products: List<ProductDTO>?) {
        _products.value = products
    }
}