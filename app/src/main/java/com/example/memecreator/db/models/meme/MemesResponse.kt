package com.example.memecreator.db.models.meme


import com.google.gson.annotations.SerializedName

data class MemesResponse(
    val success: Boolean,
    val `data`: Data
)