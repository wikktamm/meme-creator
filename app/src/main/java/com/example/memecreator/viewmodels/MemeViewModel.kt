package com.example.memecreator.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.memecreator.db.models.meme.Meme
import com.example.memecreator.db.models.meme.MemeLocal
import com.example.memecreator.db.models.meme.MemesResponse
import com.example.memecreator.repositories.MemeRepository
import com.example.memecreator.utils.Constants.FORMAT_SAVED_MEME
import com.example.memecreator.utils.Resource
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File


class MemeViewModel(private val repo: MemeRepository, application: Application) :
    AndroidViewModel(application) {
    val memesData: MutableLiveData<Resource<MemesResponse>> = MutableLiveData()
    val chosenMeme: MutableLiveData<Meme> = MutableLiveData()
    val savingMemeResult: MutableLiveData<Resource<Any>> = MutableLiveData()

    fun getAllSavedMemes() = repo.getSavedMemes()

    fun deleteMeme(meme: MemeLocal) {
        viewModelScope.launch {
            repo.deleteMeme(meme)
        }
    }

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
    fun saveMemeExternally(
        photoEditor: PhotoEditor,
        photoEditorView: PhotoEditorView
    ) {
        val file = File(
            getApplication<Application>().getExternalFilesDir(null)!!.absolutePath.toString()
                    + File.separator
                    + System.currentTimeMillis() + FORMAT_SAVED_MEME
        )
        viewModelScope.launch {
            repo.saveMemeExternally(
                photoEditor,
                photoEditorView,
                file
            ) { wasSuccess, uri ->
                when (wasSuccess) {
                    true -> {
                        viewModelScope.launch {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val contentValues = ContentValues().apply {
                                    put(
                                        MediaStore.MediaColumns.DISPLAY_NAME,
                                        System.currentTimeMillis().toString()
                                    )
                                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                                }
                                getApplication<Application>().contentResolver.update(
                                    Uri.parse(uri),
                                    contentValues,
                                    null,
                                    null
                                )
                            } else {
                                repo.saveMemeLocally(MemeLocal(uri!!))
                                savingMemeResult.postValue(Resource.Success(Any()))
                                getApplication<Application>().sendBroadcast(
                                    Intent(
                                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                        Uri.fromFile(file)
                                    )
                                )
                            }
                        }
                    }
                    false -> savingMemeResult.postValue(Resource.Error(Any(), ""))
                }
            }
        }

    }
}