package com.example.memecreator.repositories

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.example.memecreator.db.api.RetrofitInstance
import com.example.memecreator.utils.Resource
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.SaveSettings
import kotlinx.coroutines.*
import java.io.File

class MemeRepository {
    suspend fun getAllMemes() =
        RetrofitInstance().api.getAllMemes()

    fun saveMemeAndGetResponse() {

    }
    //todo bug
    @SuppressLint("MissingPermission")
    fun saveMemeInternally(
        photoEditor: PhotoEditor,
        photoEditorView: PhotoEditorView,
        file: File
    ): Resource<Any> {
        var wasSuccess = true
        try {
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build()
            photoEditor.saveAsFile(
                file.absolutePath,
                saveSettings,
                object : PhotoEditor.OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        val mSaveImageUri = Uri.fromFile(File(imagePath))
                        photoEditorView.source.setImageURI(mSaveImageUri)
                        wasSuccess = true
//                        saveToLocalDB(uri)
                        Log.d("123", "yes Success $1")
                    }

                    override fun onFailure(exception: Exception) {
                        wasSuccess=false
                        Log.d("123", "no Success $wasSuccess")
                    }
                })
        } catch (e: Exception) {
        }
        Thread.sleep(700L)

//        Log.d("123", "wasSuccess $wasSuccess")
        if (wasSuccess) return Resource.Success(Any())
        return Resource.Error(Any(), "null")

    }
}
