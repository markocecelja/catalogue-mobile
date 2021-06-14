package com.mcecelja.catalogue.services

import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {

    @GET("products")
    fun getProducts(): Call<ResponseMessage<List<ProductDTO>>>
}