package com.mcecelja.catalogue.services

import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductService {

    @GET("products")
    fun getProducts(): Call<ResponseMessage<List<ProductDTO>>>

    @POST("products/{id}/favourite")
    fun changeFavouriteStatus(@Path("id") id: Long): Call<ResponseMessage<ProductDTO>>
}