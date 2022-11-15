package ru.mixail_akulov.homework_6.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor(){

    companion object {
        private const val BASE_URL = "http://mixerxb6.beget.tech/"

        private var apiClient: ApiClient? = null
        private var retrofit: Retrofit? = null

        val instance: ApiClient?
            @Synchronized get() {
                if (apiClient == null) {
                    apiClient = ApiClient()
                }
                return apiClient
            }
    }

    init {
        retrofit =
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val api: ApiInterface
        get() = retrofit!!.create(ApiInterface::class.java)
}