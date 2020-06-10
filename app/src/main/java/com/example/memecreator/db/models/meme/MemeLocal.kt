package com.example.memecreator.db.models.meme

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class MemeLocal(val uri: Uri, val name:String) {
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}