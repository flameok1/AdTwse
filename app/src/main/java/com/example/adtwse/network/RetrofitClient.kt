package com.example.adtwse.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// network/RetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "https://openapi.twse.com.tw/"

    val instance: TwseApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // 自動解析 JSON
            .build()

        retrofit.create(TwseApiService::class.java)
    }
}