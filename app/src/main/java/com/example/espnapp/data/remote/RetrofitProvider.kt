package com.example.espnapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    // Base hosts. We'll pass full paths in @Url to hit specific endpoints.
    private const val BASE = "https://site.api.espn.com/"
    val site: Retrofit = Retrofit.Builder()
        .baseUrl(BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private const val CORE = "https://sports.core.api.espn.com/"
    val core: Retrofit = Retrofit.Builder()
        .baseUrl(CORE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
