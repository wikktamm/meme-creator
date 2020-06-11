package com.example.memecreator.db.models.meme

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memes")
data class MemeLocal(val uri: String) : MemeTemplate(uri), Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}