package com.example.memecreator.db.api

import com.example.memecreator.db.models.meme.MemesResponse
import retrofit2.Response
import retrofit2.http.GET

interface MemesApi {
    @GET("get_memes")
    suspend fun getAllMemes() : Response<MemesResponse>
}