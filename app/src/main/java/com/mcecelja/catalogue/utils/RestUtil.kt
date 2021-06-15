package com.mcecelja.catalogue.utils

import com.mcecelja.catalogue.enums.EnvironmentEnum
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestUtil {

    companion object {
        private val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(EnvironmentEnum.LOCAL_NETWORK.url)
            .addConverterFactory(GsonConverterFactory.create())

        private var retrofit = builder.build()

        private val httpClient = OkHttpClient.Builder()

        fun <S> createService(serviceClass: Class<S>): S {
            return retrofit.create(serviceClass)
        }

        fun <S> createService(serviceClass: Class<S>, token: String?): S {
            if (token != null && token.isNotEmpty()) {
                httpClient.interceptors().clear()
                httpClient.addInterceptor { chain ->
                    val original: Request = chain.request()
                    val request: Request = original.newBuilder()
                        .header("Authorization", String.format("Bearer %s", token))
                        .build()
                    chain.proceed(request)
                }
                builder.client(httpClient.build())
                retrofit = builder.build()
            }
            return retrofit.create(serviceClass)
        }
    }
}