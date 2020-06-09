package com.example.memecreator.db.api

import com.example.memecreator.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val retrofit by lazy{
        val client = OkHttpClient().newBuilder().build()
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.API_BASE_URL).client(client).build()
    }

    val api by lazy{
        retrofit.create(MemesApi::class.java)
    }
}