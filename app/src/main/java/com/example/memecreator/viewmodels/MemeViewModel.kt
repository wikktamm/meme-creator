package com.example.memecreator.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memecreator.db.api.MemeRepository
import com.example.memecreator.db.models.meme.MemesResponse
import com.example.memecreator.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MemeViewModel(val repo: MemeRepository) : ViewModel() {
   val memesData: MutableLiveData<Resource<MemesResponse>> = MutableLiveData()

    fun getAllMemes() = viewModelScope.launch {
        memesData.postValue(Resource.Loading())
        val response = repo.getAllMemes()
        memesData.postValue(handleAllMemes(response))
    }

    private fun handleAllMemes(response: Response<MemesResponse>): Resource<MemesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.body(), response.message())
    }
}