package com.example.memecreator.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.memecreator.repositories.MemeRepository
import com.example.memecreator.db.models.meme.Meme
import com.example.memecreator.db.models.meme.MemesResponse
import com.example.memecreator.utils.Resource
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class MemeViewModel(val repo: MemeRepository, application: Application) : AndroidViewModel(application) {
    val memesData: MutableLiveData<Resource<MemesResponse>> = MutableLiveData()
    val chosenMeme: MutableLiveData<Meme> = MutableLiveData()

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

    fun rememberChosenMeme(meme: Meme) {
        chosenMeme.postValue(meme)
    }

    @SuppressLint("MissingPermission")
    fun saveMemeInternally(
        photoEditor: PhotoEditor,
        photoEditorView: PhotoEditorView,
        file: File
    ) {
        viewModelScope.launch {
//            savingMeme.postValue(Resource.Loading())
            val response = repo.saveMemeInternally(photoEditor, photoEditorView, file)
            getApplication<Application>().sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file)
                )
            )
//            savingMeme.postValue(response)
        }

    }

}