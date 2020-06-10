package com.example.memecreator.db.models.meme


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Meme(
    val id: String,
    val name: String,
    val url: String,
    val width: Int,
    val height: Int,
    @SerializedName("box_count")
    val boxCount: Int
) : Serializable