package com.mcecelja.catalogue.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlacesUtil {

    companion object {
        private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())

        private val httpClient = OkHttpClient.Builder().cache(null).build()

        private var retrofit = builder.client(httpClient).build()

        fun <S> createService(serviceClass: Class<S>): S {
            return retrofit.create(serviceClass)
        }
    }
}