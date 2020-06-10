package com.example.memecreator.db.models.meme


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MemesResponse(
    val success: Boolean,
    val `data`: Data
)