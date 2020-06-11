package com.example.memecreator.repositories

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.memecreator.db.api.RetrofitInstance
import com.example.memecreator.db.local.MemesDatabase
import com.example.memecreator.db.models.meme.MemeLocal
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.SaveSettings
import java.io.File

class MemeRepository(val database: MemesDatabase) {
    suspend fun getAllMemes() =
        RetrofitInstance().api.getAllMemes()

    fun getSavedMemes() =
        database.getMemesDao().getSavedMemes()

    suspend fun saveMemeLocally(meme: MemeLocal) {
        database.getMemesDao().saveMeme(meme)
    }
    suspend fun deleteMeme(meme:MemeLocal){
        database.getMemesDao().deleteMeme(meme)
    }

    @SuppressLint("MissingPermission")
    fun saveMemeExternally(
        photoEditor: PhotoEditor,
        photoEditorView: PhotoEditorView,
        file: File, onFinishCallback: (success:Boolean, uri:String?) -> Unit
    ){
        var wasSuccess: Boolean
        var memeUriString: String
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
                        val memeUri = Uri.fromFile(File(imagePath))
                        memeUriString = memeUri.toString()
                        photoEditorView.source.setImageURI(memeUri)
                        wasSuccess = true
                        onFinishCallback(wasSuccess, memeUriString)
                        Log.d("123", memeUriString)
                    }

                    override fun onFailure(exception: Exception) {
                        wasSuccess = false
                        Log.d("123", "no Success $wasSuccess")
                        onFinishCallback(wasSuccess, null)
                    }
                })
        } catch (e: Exception) {
        }
    }
}
