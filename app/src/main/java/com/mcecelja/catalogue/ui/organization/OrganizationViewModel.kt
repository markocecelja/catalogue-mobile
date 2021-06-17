package com.mcecelja.catalogue.ui.organization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcecelja.catalogue.data.dto.product.ProductDTO

class OrganizationViewModel : ViewModel() {

    private val _product: MutableLiveData<ProductDTO> = MutableLiveData<ProductDTO>()
    val product: LiveData<ProductDTO> = _product

    fun setProduct(product: ProductDTO) {
        _product.value = product
    }
}