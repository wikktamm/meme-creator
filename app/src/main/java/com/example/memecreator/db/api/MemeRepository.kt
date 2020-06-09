package com.example.memecreator.db.api

class MemeRepository() {
    suspend fun getAllMemes() =
        RetrofitInstance().api.getAllMemes()

}